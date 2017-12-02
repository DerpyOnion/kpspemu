package com.soywiz.kpspemu.util.io

import com.soywiz.klogger.Logger
import com.soywiz.korio.async.asyncGenerate
import com.soywiz.korio.coroutine.withCoroutineContext
import com.soywiz.korio.error.invalidOp
import com.soywiz.korio.lang.ASCII
import com.soywiz.korio.lang.LATIN1
import com.soywiz.korio.lang.UTF8
import com.soywiz.korio.lang.format
import com.soywiz.korio.stream.*
import com.soywiz.korio.vfs.Vfs
import com.soywiz.korio.vfs.VfsFile
import com.soywiz.korio.vfs.VfsOpenMode
import com.soywiz.korio.vfs.VfsStat

suspend fun IsoVfs2(file: VfsFile): VfsFile = ISO2.openVfs(file.open(VfsOpenMode.READ))
suspend fun IsoVfs2(s: AsyncStream): VfsFile = ISO2.openVfs(s)
suspend fun AsyncStream.openAsIso2(): VfsFile {
	val iso = IsoVfs2(this)
	for (file in iso.listRecursive()) {
		println(file)
	}
	return iso
}
suspend fun VfsFile.openAsIso2() = IsoVfs2(this)

object ISO2 {
	val logger = Logger("ISO2")

	val CHARSET = ASCII
	//val CHARSET = UTF8

	const val SECTOR_SIZE = 0x800L

	val SCE_LBN_REGEX = Regex("^/?sce_lbn0x([0-9a-fA-F]+)_size0x([0-9a-fA-F]+)$")

	suspend fun read(s: AsyncStream): IsoFile = IsoReader(s).read()

	suspend fun openVfs(s: AsyncStream): VfsFile = withCoroutineContext {
		val iso = read(s)
		return@withCoroutineContext (object : Vfs() {
			val vfs = this
			val isoFile = iso

			fun getVfsStat(file: IsoFile): VfsStat = createExistsStat(file.fullname, isDirectory = file.isDirectory, size = file.size, extraInfo = intArrayOf(file.record.extent, 0, 0, 0, 0, 0))

			suspend override fun stat(path: String): VfsStat = try {
				getVfsStat(isoFile[path])
			} catch (e: Throwable) {
				createNonExistsStat(path)
			}


			suspend override fun open(path: String, mode: VfsOpenMode): AsyncStream {
				println("Opening ISO path: $path")
				val result = SCE_LBN_REGEX.matchEntire(path)
				if (result != null) {
					val (_, lbnString, sizeString) = result.groupValues
					val lbn = lbnString.toInt(16)
					val size = sizeString.toInt(16)
					println("Matching sce_lbn: ${result.groupValues} : $lbn, $size")
					return isoFile.reader.getSector(lbn, size)
				} else {
					return isoFile[path].open2(mode)
				}
			}

			suspend override fun list(path: String) = asyncGenerate(this@withCoroutineContext) {
				val file = isoFile[path]
				for (c in file.children) {
					//yield(getVfsStat(c))
					yield(vfs[c.fullname])
				}
			}
		}).root
	}

	class IsoReader(val s: AsyncStream) {
		suspend fun getSector(sector: Int, size: Int): AsyncStream = s.sliceWithSize(sector.toLong() * SECTOR_SIZE, size.toLong())
		suspend fun getSectorMemory(sector: Int, size: Int = SECTOR_SIZE.toInt()) = getSector(sector, size).readAvailable().openSync()

		suspend fun read(): IsoFile {
			val primary = PrimaryVolumeDescriptor(getSectorMemory(0x10, SECTOR_SIZE.toInt()))
			var udfFileSystem = false

			// http://wiki.osdev.org/UDF
			for (n in 0 until 0x10) {
				val s = getSectorMemory(0x11 + n, SECTOR_SIZE.toInt())
				val vdh = VolumeDescriptorHeader(s.clone())
				//println(vdh.id)
				when (vdh.id) {
					"CD001" -> Unit
					"BEA01" -> Unit
					"NSR02" -> udfFileSystem = true
					"NSR03" -> udfFileSystem = true
					"BOOT2" -> Unit
					"TEA01" -> Unit
				}
				//if (vdh.type == VolumeDescriptorHeader.TypeEnum.VolumePartitionSetTerminator) break
			}

			//println(udfFileSystem)

			if (udfFileSystem) {
				val udfs = getSectorMemory(0x100)
				val avd = UdfAnchorVolumeDescriptorPointer(udfs)

				val mvd = getSectorMemory(avd.mainVolumeDescriptorSequenceExtent.location)

				val pv = UdfPrimaryVolumeDescriptor(mvd)

				if (pv.descriptorTag.tagId != UdfDescriptorTag.TagId.PRIMARY_VOLUME_DESCRIPTOR) {
					invalidOp("Expected UDF primary volume descriptor")
				}

				//println(pv)
				//println(avd)
				//println(avd)
			}

			val root = IsoFile(this@IsoReader, primary.rootDirectoryRecord, null)
			readDirectoryRecords(root, getSectorMemory(primary.rootDirectoryRecord.extent, primary.rootDirectoryRecord.size))
			return root
		}

		suspend fun readDirectoryRecords(parent: IsoFile, sector: SyncStream): Unit {
			while (!sector.eof) {
				val dr = DirectoryRecord(sector)
				if (dr == null) {
					sector.skipToAlign(SECTOR_SIZE.toInt())
					continue
				}
				if (dr.name == "" || dr.name == "\u0001") continue
				val file = IsoFile(this@IsoReader, dr, parent)
				logger.info { "IsoFile: ${file.fullname}" }

				if (dr.isDirectory) readDirectoryRecords(file, getSectorMemory(dr.extent, dr.size))
			}
		}
	}

	class IsoFile(val reader: IsoReader, val record: DirectoryRecord, val parent: IsoFile?) {
		val name: String get() = record.name
		val isDirectory: Boolean get() = record.isDirectory
		val fullname: String = if (parent == null) record.name else "${parent.fullname}/${record.name}".trimStart('/')
		val children = arrayListOf<IsoFile>()
		val size: Long = record.size.toLong()

		init {
			parent?.children?.add(this)
		}

		fun dump() {
			println("$fullname: $record")
			for (c in children) c.dump()
		}

		suspend fun open2(mode: VfsOpenMode) = reader.getSector(record.extent, record.size)
		operator fun get(name: String): IsoFile {
			var current = this
			for (part in name.split("/")) {
				when (part) {
					"" -> Unit
					"." -> Unit
					".." -> current = current.parent!!
					else -> current = current.children.firstOrNull { it.name.toUpperCase() == part.toUpperCase() } ?: throw IllegalStateException("Can't find part $part for accessing path $name children: ${current.children}")
				}
			}
			return current
		}

		override fun toString(): String {
			return "IsoFile(fullname='$fullname', size=$size)"
		}
	}

	fun SyncStream.readLongArray_le(count: Int): LongArray = (0 until count).map { readS64_le() }.toLongArray()

	fun SyncStream.readU32_le_be(): Int {
		val le = readS32_le()
		readS32_be()
		return le
	}

	fun SyncStream.readTextWithLength(): String {
		val len = readU8()
		return readStringz(len, CHARSET)
	}

	fun SyncStream.readU16_le_be(): Int {
		val le = readS16_le()
		readS16_be()
		return le
	}

	data class UdfDescriptorTag(
		val tagId: TagId,
		val descVersion: Int,
		val tagChecksum: Int,
		val reserved: Int,
		val tagSerialNumber: Int,
		val descriptorCRC: Int,
		val descriptorCRCLength: Int,
		val tagLocation: Int
	) {
		data class TagId(val id: Int) {
			companion object {
				val PRIMARY_VOLUME_DESCRIPTOR = TagId(0x0001)
				val ANCHOR_VOLUME_DESCRIPTOR_POINTER = TagId(0x0002)
				val VOLUME_DESCRIPTOR_POINTER = TagId(0x0003)
				val IMPLEMENTATION_USE_VOLUME_DESCRIPTOR = TagId(0x0004)
				val PARTITION_DESCRIPTOR = TagId(0x0005)
				val LOGICAL_VOLUME_DESCRIPTOR = TagId(0x0006)
				val UNALLOCATED_SPACE_DESCRIPTOR = TagId(0x0007)
				val TERMINATING_DESCRIPTOR = TagId(0x0008)
				val LOGICAL_VOLUME_INTEGRITY_DESCRIPTOR = TagId(0x0009)
				val FILE_SET_DESCRIPTOR = TagId(0x0100)
				val FILE_IDENTIFIER_DESCRIPTOR = TagId(0x0101)
				val ALLOCATION_EXTENT_DESCRIPTOR = TagId(0x0102)
				val INDIRECT_ENTRY = TagId(0x0103)
				val TERMINAL_ENTRY = TagId(0x0104)
				val FILE_ENTRY = TagId(0x0105)
				val EXTENDED_ATTRIBUTE_HEADER_DESCRIPTOR = TagId(0x0106)
				val UNALLOCATED_SPACE_ENTRY = TagId(0x0107)
				val SPACE_BITMAP_DESCRIPTOR = TagId(0x0108)
				val PARTITION_INTEGRITY_ENTRY = TagId(0x0109)
				val EXTENDED_FILE_ENTRY = TagId(0x010a)
			}
		}

		constructor(s: SyncStream) : this(
			tagId = TagId(s.readU16_le()),
			descVersion = s.readU16_le(),
			tagChecksum = s.readU8(),
			reserved = s.readU8(),
			tagSerialNumber = s.readU16_le(),
			descriptorCRC = s.readU16_le(),
			descriptorCRCLength = s.readU16_le(),
			tagLocation = s.readS32_le()
		)
	}

	data class UdfExtent(
		val length: Int,
		val location: Int
	) {
		constructor(s: SyncStream) : this(
			length = s.readS32_le(),
			location = s.readS32_le()
		)
	}

	data class UdfAnchorVolumeDescriptorPointer(
		val descriptorTag: UdfDescriptorTag,
		val mainVolumeDescriptorSequenceExtent: UdfExtent,
		val reserveVolumeDescriptorSequenceExtent: UdfExtent
	) {
		constructor(s: SyncStream) : this(
			descriptorTag = UdfDescriptorTag(s),
			mainVolumeDescriptorSequenceExtent = UdfExtent(s),
			reserveVolumeDescriptorSequenceExtent = UdfExtent(s)
		)
	}

	data class UdfCharspec(
		val characterSetType: Int,
		val characterSetInfo: String
	) {
		constructor(s: SyncStream) : this(
			characterSetType = s.readU8(),
			characterSetInfo = s.readStringz(63, CHARSET)
		)
	}

	data class UdfEntityId(
		val flags: Int,
		val identifier: String,
		val identifierSuffix: String
	) {
		constructor(s: SyncStream) : this(
			flags = s.readU8(),
			identifier = s.readStringz(23, CHARSET),
			identifierSuffix = s.readStringz(8, CHARSET)
		)
	}

	data class UdfTimestamp(
		val typeAndTimezone: Int,
		val year: Int,
		val month: Int,
		val day: Int,
		val hour: Int,
		val minute: Int,
		val second: Int,
		val centiseconds: Int,
		val hundredsofMicroseconds: Int,
		val microseconds: Int
	) {
		constructor(s: SyncStream) : this(
			typeAndTimezone = s.readS16_le(),
			year = s.readS16_le(),
			month = s.readU8(),
			day = s.readU8(),
			hour = s.readU8(),
			minute = s.readU8(),
			second = s.readU8(),
			centiseconds = s.readU8(),
			hundredsofMicroseconds = s.readU8(),
			microseconds = s.readU8()
		)
	}

	data class UdfPrimaryVolumeDescriptor(
		val descriptorTag: UdfDescriptorTag,
		val volumeDescriptorSequenceNumber: Int,
		val primaryVolumeDescriptorNumber: Int,
		val volumeId: String,
		val volumeSequenceNumber: Int,
		val maximumVolumeSequenceNumber: Int,
		val interchangeLevel: Int,
		val maximumInterchangeLevel: Int,
		val characterSetList: Int,
		val maximumCharacterSetList: Int,
		val volumeSetIdentifier: String,
		val descriptorCharacterSet: UdfCharspec,
		val explanatoryCharacterSet: UdfCharspec,
		val volumeAbstract: UdfExtent,
		val volumeCopyrightNotice: UdfExtent,
		val applicationIdentifier: UdfEntityId,
		val recordingDateandTime: UdfTimestamp,
		val implementationIdentifier: UdfEntityId,
		val implementationUse: ByteArray,
		val predecessorVolumeDescriptorSequenceLocation: Int,
		val flags: Int
	) {
		constructor(s: SyncStream) : this(
			descriptorTag = UdfDescriptorTag(s),
			volumeDescriptorSequenceNumber = s.readS32_le(),
			primaryVolumeDescriptorNumber = s.readS32_le(),
			volumeId = s.readUdfDString(32),
			volumeSequenceNumber = s.readU16_le(),
			maximumVolumeSequenceNumber = s.readU16_le(),
			interchangeLevel = s.readU16_le(),
			maximumInterchangeLevel = s.readU16_le(),
			characterSetList = s.readS32_le(),
			maximumCharacterSetList = s.readS32_le(),
			volumeSetIdentifier = s.readUdfDString(128),
			descriptorCharacterSet = UdfCharspec(s),
			explanatoryCharacterSet = UdfCharspec(s),
			volumeAbstract = UdfExtent(s),
			volumeCopyrightNotice = UdfExtent(s),
			applicationIdentifier = UdfEntityId(s),
			recordingDateandTime = UdfTimestamp(s),
			implementationIdentifier = UdfEntityId(s),
			implementationUse = s.readBytesExact(64),
			predecessorVolumeDescriptorSequenceLocation = s.readS32_le(),
			flags = s.readU16_le()
		)
	}

	data class PrimaryVolumeDescriptor(
		val volumeDescriptorHeader: VolumeDescriptorHeader,
		val pad1: Int,
		val systemId: String,
		val volumeId: String,
		val pad2: Long,
		val volumeSpaceSize: Int,
		val pad3: LongArray,
		val volumeSetSize: Int,
		val volumeSequenceNumber: Int,
		val logicalBlockSize: Int,
		val pathTableSize: Int,
		val typeLPathTable: Int,
		val optType1PathTable: Int,
		val typeMPathTable: Int,
		val optTypeMPathTable: Int,
		val rootDirectoryRecord: DirectoryRecord,
		val volumeSetId: String,
		val publisherId: String,
		val preparerId: String,
		val applicationId: String,
		val copyrightFileId: String,
		val abstractFileId: String,
		val bibliographicFileId: String,
		val creationDate: IsoDate,
		val modificationDate: IsoDate,
		val expirationDate: IsoDate,
		val effectiveDate: IsoDate,
		val fileStructureVersion: Int,
		val pad5: Int,
		val applicationData: ByteArray,
		val pad6: ByteArray
		//fixed byte Pad6_[653];
	) {
		constructor(s: SyncStream) : this(
			volumeDescriptorHeader = VolumeDescriptorHeader(s),
			pad1 = s.readU8(),
			systemId = s.readStringz(0x20, CHARSET),
			volumeId = s.readStringz(0x20, CHARSET),
			pad2 = s.readS64_le(),
			volumeSpaceSize = s.readU32_le_be(),
			pad3 = s.readLongArray_le(4),
			volumeSetSize = s.readU16_le_be(),
			volumeSequenceNumber = s.readU16_le_be(),
			logicalBlockSize = s.readU16_le_be(),
			pathTableSize = s.readU32_le_be(),
			typeLPathTable = s.readS32_le(),
			optType1PathTable = s.readS32_le(),
			typeMPathTable = s.readS32_le(),
			optTypeMPathTable = s.readS32_le(),
			rootDirectoryRecord = DirectoryRecord(s)!!,
			volumeSetId = s.readStringz(0x80, CHARSET),
			publisherId = s.readStringz(0x80, CHARSET),
			preparerId = s.readStringz(0x80, CHARSET),
			applicationId = s.readStringz(0x80, CHARSET),
			copyrightFileId = s.readStringz(37, CHARSET),
			abstractFileId = s.readStringz(37, CHARSET),
			bibliographicFileId = s.readStringz(37, CHARSET),
			creationDate = IsoDate(s),
			modificationDate = IsoDate(s),
			expirationDate = IsoDate(s),
			effectiveDate = IsoDate(s),
			fileStructureVersion = s.readU8(),
			pad5 = s.readU8(),
			applicationData = s.readBytes(0x200),
			pad6 = s.readBytes(653)
		) {
			//println(this)
		}
	}

	data class VolumeDescriptorHeader(
		val type: TypeEnum,
		val id: String,
		val version: Int
	) {
		data class TypeEnum(val id: Int) {
			companion object {
				val BootRecord = TypeEnum(0x00)
				val VolumePartitionSetTerminator = TypeEnum(0xFF)
				val PrimaryVolumeDescriptor = TypeEnum(0x01)
				val SupplementaryVolumeDescriptor = TypeEnum(0x02)
				val VolumePartitionDescriptor = TypeEnum(0x03)

				//val BY_ID = values().associateBy { it.id }
			}
		}

		constructor(s: SyncStream) : this(
			type = TypeEnum(s.readU8()),
			id = s.readStringz(5, CHARSET),
			version = s.readU8()
		)
	}

	data class IsoDate(val data: String) {
		constructor(s: SyncStream) : this(data = s.readString(17, ASCII))

		val year = data.substring(0, 4).toIntOrNull() ?: 0
		val month = data.substring(4, 6).toIntOrNull() ?: 0
		val day = data.substring(6, 8).toIntOrNull() ?: 0
		val hour = data.substring(8, 10).toIntOrNull() ?: 0
		val minute = data.substring(10, 12).toIntOrNull() ?: 0
		val second = data.substring(12, 14).toIntOrNull() ?: 0
		val hsecond = data.substring(14, 16).toIntOrNull() ?: 0
		//val offset = data.substring(16).toInt()

		override fun toString(): String = "IsoDate(%04d-%02d-%02d %02d:%02d:%02d.%d)".format(year, month, day, hour, minute, second, hsecond)
	}

	data class DateStruct(
		val year: Int,
		val month: Int,
		val day: Int,
		val hour: Int,
		val minute: Int,
		val second: Int,
		val offset: Int
	) {
		constructor(s: SyncStream) : this(
			year = s.readU8(),
			month = s.readU8(),
			day = s.readU8(),
			hour = s.readU8(),
			minute = s.readU8(),
			second = s.readU8(),
			offset = s.readU8()
		)

		val fullYear = 1900 + year
	}

	data class DirectoryRecord(
		val length: Int,
		val extendedAttributeLength: Int,
		val extent: Int,
		val size: Int,
		val date: DateStruct,
		val flags: Int,
		val fileUnitSize: Int,
		val interleave: Int,
		val volumeSequenceNumber: Int,
		val rawName: String
	) {
		val name = rawName.substringBefore(';')
		val offset: Long = extent.toLong() * SECTOR_SIZE
		val isDirectory = (flags and 2) != 0

		companion object {
			operator fun invoke(_s: SyncStream): DirectoryRecord? {
				val length = _s.readU8()
				if (length <= 0) {
					return null
				} else {
					val s = _s.readStream((length - 1).toLong())

					val dr = DirectoryRecord(
						length = length,
						extendedAttributeLength = s.readU8(),
						extent = s.readU32_le_be(),
						size = s.readU32_le_be(),
						date = DateStruct(s),
						flags = s.readU8(),
						fileUnitSize = s.readU8(),
						interleave = s.readU8(),
						volumeSequenceNumber = s.readU16_le_be(),
						rawName = s.readTextWithLength()
					)

					//println("DR: $dr, ${s.available}")

					return dr
				}
			}
		}
	}
}

fun SyncStream.readUdfDString(bytes: Int): String {
	val ss = readStream(bytes)
	val count = ss.readU16_le() / 2
	//println("readUdfDString($bytes, $count)")
	return ss.readUtf16_le(count)
}

fun SyncStream.readUtf16_le(count: Int): String {
	var s = ""
	for (n in 0 until count) {
		s += readS16_le().toChar()
		//println("S($count): $s")
	}
	return s
}