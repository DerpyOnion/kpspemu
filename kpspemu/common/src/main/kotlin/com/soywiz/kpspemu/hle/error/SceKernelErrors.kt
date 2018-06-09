@file:Suppress("MayBeConstant", "RemoveRedundantCallsOfConversionMethods")

package com.soywiz.kpspemu.hle.error

import com.soywiz.klogger.*

class SceKernelException(val errorCode: Int) : Exception()

private val errorLogger = Logger("SceKernelException")

fun sceKernelException(errorCode: Int = SceKernelErrors.ERROR_ERROR): Nothing {
    errorLogger.info { "ERROR: $errorCode" }
    throw SceKernelException(errorCode)
}

@Suppress("unused")
object SceKernelErrors {
    val ERROR_OK: Int = 0x00000000.toInt()
    val ERROR_ERROR: Int = 0x80020001.toInt()
    val ERROR_NOTIMP: Int = 0x80020002.toInt()
    val ERROR_ALREADY: Int = 0x80000020.toInt()
    val ERROR_BUSY: Int = 0x80000021.toInt()
    val ERROR_OUT_OF_MEMORY: Int = 0x80000022.toInt()
    val ERROR_INVALID_ID: Int = 0x80000100.toInt()
    val ERROR_INVALID_NAME: Int = 0x80000101.toInt()
    val ERROR_INVALID_INDEX: Int = 0x80000102.toInt()
    val ERROR_INVALID_POINTER: Int = 0x80000103.toInt()
    val ERROR_INVALID_SIZE: Int = 0x80000104.toInt()
    val ERROR_INVALID_FLAG: Int = 0x80000105.toInt()
    val ERROR_INVALID_COMMAND: Int = 0x80000106.toInt()
    val ERROR_INVALID_MODE: Int = 0x80000107.toInt()
    val ERROR_INVALID_FORMAT: Int = 0x80000108.toInt()
    val ERROR_INVALID_VALUE: Int = 0x800001FE.toInt()
    val ERROR_INVALID_ARGUMENT: Int = 0x800001FF.toInt()
    val ERROR_BAD_FILE: Int = 0x80000209.toInt()
    val ERROR_ACCESS_ERROR: Int = 0x8000020D.toInt()
    val ERROR_ERRNO_OPERATION_NOT_PERMITTED: Int = 0x80010001.toInt()
    val ERROR_ERRNO_FILE_NOT_FOUND: Int = 0x80010002.toInt()
    val ERROR_ERRNO_FILE_OPEN_ERROR: Int = 0x80010003.toInt()
    val ERROR_ERRNO_IO_ERROR: Int = 0x80010005.toInt()
    val ERROR_ERRNO_ARG_LIST_TOO_LONG: Int = 0x80010007.toInt()
    val ERROR_ERRNO_INVALID_FILE_DESCRIPTOR: Int = 0x80010009.toInt()
    val ERROR_ERRNO_RESOURCE_UNAVAILABLE: Int = 0x8001000B.toInt()
    val ERROR_ERRNO_NO_MEMORY: Int = 0x8001000C.toInt()
    val ERROR_ERRNO_NO_PERM: Int = 0x8001000D.toInt()
    val ERROR_ERRNO_FILE_INVALID_ADDR: Int = 0x8001000E.toInt()
    val ERROR_ERRNO_DEVICE_BUSY: Int = 0x80010010.toInt()
    val ERROR_ERRNO_FILE_ALREADY_EXISTS: Int = 0x80010011.toInt()
    val ERROR_ERRNO_CROSS_DEV_LINK: Int = 0x80010012.toInt()
    val ERROR_ERRNO_DEVICE_NOT_FOUND: Int = 0x80010013.toInt()
    val ERROR_ERRNO_NOT_A_DIRECTORY: Int = 0x80010014.toInt()
    val ERROR_ERRNO_IS_DIRECTORY: Int = 0x80010015.toInt()
    val ERROR_ERRNO_INVALID_ARGUMENT: Int = 0x80010016.toInt()
    val ERROR_ERRNO_TOO_MANY_OPEN_SYSTEM_FILES: Int = 0x80010018.toInt()
    val ERROR_ERRNO_FILE_IS_TOO_BIG: Int = 0x8001001B.toInt()
    val ERROR_ERRNO_DEVICE_NO_FREE_SPACE: Int = 0x8001001C.toInt()
    val ERROR_ERRNO_READ_ONLY: Int = 0x8001001E.toInt()
    val ERROR_ERRNO_CLOSED: Int = 0x80010020.toInt()
    val ERROR_ERRNO_FILE_PATH_TOO_LONG: Int = 0x80010024.toInt()
    val ERROR_ERRNO_FILE_PROTOCOL: Int = 0x80010047.toInt()
    val ERROR_ERRNO_DIRECTORY_IS_NOT_EMPTY: Int = 0x8001005A.toInt()
    val ERROR_ERRNO_TOO_MANY_SYMBOLIC_LINKS: Int = 0x8001005C.toInt()
    val ERROR_ERRNO_FILE_ADDR_IN_USE: Int = 0x80010062.toInt()
    val ERROR_ERRNO_CONNECTION_ABORTED: Int = 0x80010067.toInt()
    val ERROR_ERRNO_CONNECTION_RESET: Int = 0x80010068.toInt()
    val ERROR_ERRNO_NO_FREE_BUF_SPACE: Int = 0x80010069.toInt()
    val ERROR_ERRNO_FILE_TIMEOUT: Int = 0x8001006E.toInt()
    val ERROR_ERRNO_IN_PROGRESS: Int = 0x80010077.toInt()
    val ERROR_ERRNO_ALREADY: Int = 0x80010078.toInt()
    val ERROR_ERRNO_NO_MEDIA: Int = 0x8001007B.toInt()
    val ERROR_ERRNO_INVALID_MEDIUM: Int = 0x8001007C.toInt()
    val ERROR_ERRNO_ADDRESS_NOT_AVAILABLE: Int = 0x8001007D.toInt()
    val ERROR_ERRNO_IS_ALREADY_CONNECTED: Int = 0x8001007F.toInt()
    val ERROR_ERRNO_NOT_CONNECTED: Int = 0x80010080.toInt()
    val ERROR_ERRNO_FILE_QUOTA_EXCEEDED: Int = 0x80010084.toInt()
    val ERROR_ERRNO_FUNCTION_NOT_SUPPORTED: Int = 0x8001B000.toInt()
    val ERROR_ERRNO_ADDR_OUT_OF_MAIN_MEM: Int = 0x8001B001.toInt()
    val ERROR_ERRNO_INVALID_UNIT_NUM: Int = 0x8001B002.toInt()
    val ERROR_ERRNO_INVALID_FILE_SIZE: Int = 0x8001B003.toInt()
    val ERROR_ERRNO_INVALID_FLAG: Int = 0x8001B004.toInt()
    val ERROR_KERNEL_CANNOT_BE_CALLED_FROM_INTERRUPT: Int = 0x80020064.toInt()
    val ERROR_KERNEL_INTERRUPTS_ALREADY_DISABLED: Int = 0x80020066.toInt()
    val ERROR_KERNEL_UNKNOWN_UID: Int = 0x800200cb.toInt()
    val ERROR_KERNEL_UNMATCH_TYPE_UID: Int = 0x800200cc.toInt()
    val ERROR_KERNEL_NOT_EXIST_ID: Int = 0x800200cd.toInt()
    val ERROR_KERNEL_NOT_FOUND_FUNCTION_UID: Int = 0x800200ce.toInt()
    val ERROR_KERNEL_ALREADY_HOLDER_UID: Int = 0x800200cf.toInt()
    val ERROR_KERNEL_NOT_HOLDER_UID: Int = 0x800200d0.toInt()
    val ERROR_KERNEL_ILLEGAL_PERMISSION: Int = 0x800200d1.toInt()
    val ERROR_KERNEL_ILLEGAL_ARGUMENT: Int = 0x800200d2.toInt()
    val ERROR_KERNEL_ILLEGAL_ADDR: Int = 0x800200d3.toInt()
    val ERROR_KERNEL_MEMORY_AREA_OUT_OF_RANGE: Int = 0x800200d4.toInt()
    val ERROR_KERNEL_MEMORY_AREA_IS_OVERLAP: Int = 0x800200d5.toInt()
    val ERROR_KERNEL_ILLEGAL_PARTITION_ID: Int = 0x800200d6.toInt()
    val ERROR_KERNEL_PARTITION_IN_USE: Int = 0x800200d7.toInt()
    val ERROR_KERNEL_ILLEGAL_MEMBLOCK_ALLOC_TYPE: Int = 0x800200d8.toInt()
    val ERROR_KERNEL_FAILED_ALLOC_MEMBLOCK: Int = 0x800200d9.toInt()
    val ERROR_KERNEL_INHIBITED_RESIZE_MEMBLOCK: Int = 0x800200da.toInt()
    val ERROR_KERNEL_FAILED_RESIZE_MEMBLOCK: Int = 0x800200db.toInt()
    val ERROR_KERNEL_FAILED_ALLOC_HEAPBLOCK: Int = 0x800200dc.toInt()
    val ERROR_KERNEL_FAILED_ALLOC_HEAP: Int = 0x800200dd.toInt()
    val ERROR_KERNEL_ILLEGAL_CHUNK_ID: Int = 0x800200de.toInt()
    val ERROR_KERNEL_CANNOT_FIND_CHUNK_NAME: Int = 0x800200df.toInt()
    val ERROR_KERNEL_NO_FREE_CHUNK: Int = 0x800200e0.toInt()
    val ERROR_KERNEL_MEMBLOCK_FRAGMENTED: Int = 0x800200e1.toInt()
    val ERROR_KERNEL_MEMBLOCK_CANNOT_JOINT: Int = 0x800200e2.toInt()
    val ERROR_KERNEL_MEMBLOCK_CANNOT_SEPARATE: Int = 0x800200e3.toInt()
    val ERROR_KERNEL_ILLEGAL_ALIGNMENT_SIZE: Int = 0x800200e4.toInt()
    val ERROR_KERNEL_ILLEGAL_DEVKIT_VER: Int = 0x800200e5.toInt()
    val ERROR_KERNEL_MODULE_LINK_ERROR: Int = 0x8002012c.toInt()
    val ERROR_KERNEL_ILLEGAL_OBJECT_FORMAT: Int = 0x8002012d.toInt()
    val ERROR_KERNEL_UNKNOWN_MODULE: Int = 0x8002012e.toInt()
    val ERROR_KERNEL_UNKNOWN_MODULE_FILE: Int = 0x8002012f.toInt()
    val ERROR_KERNEL_FILE_READ_ERROR: Int = 0x80020130.toInt()
    val ERROR_KERNEL_MEMORY_IN_USE: Int = 0x80020131.toInt()
    val ERROR_KERNEL_PARTITION_MISMATCH: Int = 0x80020132.toInt()
    val ERROR_KERNEL_MODULE_ALREADY_STARTED: Int = 0x80020133.toInt()
    val ERROR_KERNEL_MODULE_NOT_STARTED: Int = 0x80020134.toInt()
    val ERROR_KERNEL_MODULE_ALREADY_STOPPED: Int = 0x80020135.toInt()
    val ERROR_KERNEL_MODULE_CANNOT_STOP: Int = 0x80020136.toInt()
    val ERROR_KERNEL_MODULE_NOT_STOPPED: Int = 0x80020137.toInt()
    val ERROR_KERNEL_MODULE_CANNOT_REMOVE: Int = 0x80020138.toInt()
    val ERROR_KERNEL_EXCLUSIVE_LOAD: Int = 0x80020139.toInt()
    val ERROR_KERNEL_LIBRARY_IS_NOT_LINKED: Int = 0x8002013a.toInt()
    val ERROR_KERNEL_LIBRARY_ALREADY_EXISTS: Int = 0x8002013b.toInt()
    val ERROR_KERNEL_LIBRARY_NOT_FOUND: Int = 0x8002013c.toInt()
    val ERROR_KERNEL_ILLEGAL_LIBRARY_HEADER: Int = 0x8002013d.toInt()
    val ERROR_KERNEL_LIBRARY_IN_USE: Int = 0x8002013e.toInt()
    val ERROR_KERNEL_MODULE_ALREADY_STOPPING: Int = 0x8002013f.toInt()
    val ERROR_KERNEL_ILLEGAL_OFFSET_VALUE: Int = 0x80020140.toInt()
    val ERROR_KERNEL_ILLEGAL_POSITION_CODE: Int = 0x80020141.toInt()
    val ERROR_KERNEL_ILLEGAL_ACCESS_CODE: Int = 0x80020142.toInt()
    val ERROR_KERNEL_MODULE_MANAGER_BUSY: Int = 0x80020143.toInt()
    val ERROR_KERNEL_ILLEGAL_FLAG: Int = 0x80020144.toInt()
    val ERROR_KERNEL_CANNOT_GET_MODULE_LIST: Int = 0x80020145.toInt()
    val ERROR_KERNEL_PROHIBIT_LOADMODULE_DEVICE: Int = 0x80020146.toInt()
    val ERROR_KERNEL_PROHIBIT_LOADEXEC_DEVICE: Int = 0x80020147.toInt()
    val ERROR_KERNEL_UNSUPPORTED_PRX_TYPE: Int = 0x80020148.toInt()
    val ERROR_KERNEL_ILLEGAL_PERMISSION_CALL: Int = 0x80020149.toInt()
    val ERROR_KERNEL_CANNOT_GET_MODULE_INFO: Int = 0x8002014a.toInt()
    val ERROR_KERNEL_ILLEGAL_LOADEXEC_BUFFER: Int = 0x8002014b.toInt()
    val ERROR_KERNEL_ILLEGAL_LOADEXEC_FILENAME: Int = 0x8002014c.toInt()
    val ERROR_KERNEL_NO_EXIT_CALLBACK: Int = 0x8002014d.toInt()
    val ERROR_KERNEL_MEDIA_CHANGED: Int = 0x8002014e.toInt()
    val ERROR_KERNEL_CANNOT_USE_BETA_VER_MODULE: Int = 0x8002014f.toInt()
    val ERROR_KERNEL_NO_MEMORY: Int = 0x80020190.toInt()
    val ERROR_KERNEL_ILLEGAL_ATTR: Int = 0x80020191.toInt()
    val ERROR_KERNEL_ILLEGAL_THREAD_ENTRY_ADDR: Int = 0x80020192.toInt()
    val ERROR_KERNEL_ILLEGAL_PRIORITY: Int = 0x80020193.toInt()
    val ERROR_KERNEL_ILLEGAL_STACK_SIZE: Int = 0x80020194.toInt()
    val ERROR_KERNEL_ILLEGAL_MODE: Int = 0x80020195.toInt()
    val ERROR_KERNEL_ILLEGAL_MASK: Int = 0x80020196.toInt()
    val ERROR_KERNEL_ILLEGAL_THREAD: Int = 0x80020197.toInt()
    val ERROR_KERNEL_NOT_FOUND_THREAD: Int = 0x80020198.toInt()
    val ERROR_KERNEL_NOT_FOUND_SEMAPHORE: Int = 0x80020199.toInt()
    val ERROR_KERNEL_NOT_FOUND_EVENT_FLAG: Int = 0x8002019a.toInt()
    val ERROR_KERNEL_NOT_FOUND_MESSAGE_BOX: Int = 0x8002019b.toInt()
    val ERROR_KERNEL_NOT_FOUND_VPOOL: Int = 0x8002019c.toInt()
    val ERROR_KERNEL_NOT_FOUND_FPOOL: Int = 0x8002019d.toInt()
    val ERROR_KERNEL_NOT_FOUND_MESSAGE_PIPE: Int = 0x8002019e.toInt()
    val ERROR_KERNEL_NOT_FOUND_ALARM: Int = 0x8002019f.toInt()
    val ERROR_KERNEL_NOT_FOUND_THREAD_EVENT_HANDLER: Int = 0x800201a0.toInt()
    val ERROR_KERNEL_NOT_FOUND_CALLBACK: Int = 0x800201a1.toInt()
    val ERROR_KERNEL_THREAD_ALREADY_DORMANT: Int = 0x800201a2.toInt()
    val ERROR_KERNEL_THREAD_ALREADY_SUSPEND: Int = 0x800201a3.toInt()
    val ERROR_KERNEL_THREAD_IS_NOT_DORMANT: Int = 0x800201a4.toInt()
    val ERROR_KERNEL_THREAD_IS_NOT_SUSPEND: Int = 0x800201a5.toInt()
    val ERROR_KERNEL_THREAD_IS_NOT_WAIT: Int = 0x800201a6.toInt()
    val ERROR_KERNEL_WAIT_CAN_NOT_WAIT: Int = 0x800201a7.toInt()
    val ERROR_KERNEL_WAIT_TIMEOUT: Int = 0x800201a8.toInt()
    val ERROR_KERNEL_WAIT_CANCELLED: Int = 0x800201a9.toInt()
    val ERROR_KERNEL_WAIT_STATUS_RELEASED: Int = 0x800201aa.toInt()
    val ERROR_KERNEL_WAIT_STATUS_RELEASED_CALLBACK: Int = 0x800201ab.toInt()
    val ERROR_KERNEL_THREAD_IS_TERMINATED: Int = 0x800201ac.toInt()
    val ERROR_KERNEL_SEMA_ZERO: Int = 0x800201ad.toInt()
    val ERROR_KERNEL_SEMA_OVERFLOW: Int = 0x800201ae.toInt()
    val ERROR_KERNEL_EVENT_FLAG_POLL_FAILED: Int = 0x800201af.toInt()
    val ERROR_KERNEL_EVENT_FLAG_NO_MULTI_PERM: Int = 0x800201b0.toInt()
    val ERROR_KERNEL_EVENT_FLAG_ILLEGAL_WAIT_PATTERN: Int = 0x800201b1.toInt()
    val ERROR_KERNEL_MESSAGEBOX_NO_MESSAGE: Int = 0x800201b2.toInt()
    val ERROR_KERNEL_MESSAGE_PIPE_FULL: Int = 0x800201b3.toInt()
    val ERROR_KERNEL_MESSAGE_PIPE_EMPTY: Int = 0x800201b4.toInt()
    val ERROR_KERNEL_WAIT_DELETE: Int = 0x800201b5.toInt()
    val ERROR_KERNEL_ILLEGAL_MEMBLOCK: Int = 0x800201b6.toInt()
    val ERROR_KERNEL_ILLEGAL_MEMSIZE: Int = 0x800201b7.toInt()
    val ERROR_KERNEL_ILLEGAL_SCRATCHPAD_ADDR: Int = 0x800201b8.toInt()
    val ERROR_KERNEL_SCRATCHPAD_IN_USE: Int = 0x800201b9.toInt()
    val ERROR_KERNEL_SCRATCHPAD_NOT_IN_USE: Int = 0x800201ba.toInt()
    val ERROR_KERNEL_ILLEGAL_TYPE: Int = 0x800201bb.toInt()
    val ERROR_KERNEL_ILLEGAL_SIZE: Int = 0x800201bc.toInt()
    val ERROR_KERNEL_ILLEGAL_COUNT: Int = 0x800201bd.toInt()
    val ERROR_KERNEL_NOT_FOUND_VTIMER: Int = 0x800201be.toInt()
    val ERROR_KERNEL_ILLEGAL_VTIMER: Int = 0x800201bf.toInt()
    val ERROR_KERNEL_ILLEGAL_KTLS: Int = 0x800201c0.toInt()
    val ERROR_KERNEL_KTLS_IS_FULL: Int = 0x800201c1.toInt()
    val ERROR_KERNEL_KTLS_IS_BUSY: Int = 0x800201c2.toInt()
    val ERROR_KERNEL_MUTEX_NOT_FOUND: Int = 0x800201c3.toInt()
    val ERROR_KERNEL_MUTEX_LOCKED: Int = 0x800201c4.toInt()
    val ERROR_KERNEL_MUTEX_UNLOCKED: Int = 0x800201c5.toInt()
    val ERROR_KERNEL_MUTEX_LOCK_OVERFLOW: Int = 0x800201c6.toInt()
    val ERROR_KERNEL_MUTEX_UNLOCK_UNDERFLOW: Int = 0x800201c7.toInt()
    val ERROR_KERNEL_MUTEX_RECURSIVE_NOT_ALLOWED: Int = 0x800201c8.toInt()
    val ERROR_KERNEL_MESSAGEBOX_DUPLICATE_MESSAGE: Int = 0x800201c9.toInt()
    val ERROR_KERNEL_LWMUTEX_NOT_FOUND: Int = 0x800201ca.toInt()
    val ERROR_KERNEL_LWMUTEX_LOCKED: Int = 0x800201cb.toInt()
    val ERROR_KERNEL_LWMUTEX_UNLOCKED: Int = 0x800201cc.toInt()
    val ERROR_KERNEL_LWMUTEX_LOCK_OVERFLOW: Int = 0x800201cd.toInt()
    val ERROR_KERNEL_LWMUTEX_UNLOCK_UNDERFLOW: Int = 0x800201ce.toInt()
    val ERROR_KERNEL_LWMUTEX_RECURSIVE_NOT_ALLOWED: Int = 0x800201cf.toInt()
    val ERROR_KERNEL_POWER_CANNOT_CANCEL: Int = 0x80020261.toInt()
    val ERROR_KERNEL_TOO_MANY_OPEN_FILES: Int = 0x80020320.toInt()
    val ERROR_KERNEL_NO_SUCH_DEVICE: Int = 0x80020321.toInt()
    val ERROR_KERNEL_BAD_FILE_DESCRIPTOR: Int = 0x80020323.toInt()
    val ERROR_KERNEL_UNSUPPORTED_OPERATION: Int = 0x80020325.toInt()
    val ERROR_KERNEL_NOCWD: Int = 0x8002032c.toInt()
    val ERROR_KERNEL_FILENAME_TOO_LONG: Int = 0x8002032d.toInt()
    val ERROR_KERNEL_ASYNC_BUSY: Int = 0x80020329.toInt()
    val ERROR_KERNEL_NO_ASYNC_OP: Int = 0x8002032a.toInt()
    val ERROR_KERNEL_NOT_CACHE_ALIGNED: Int = 0x8002044c.toInt()
    val ERROR_KERNEL_MAX_ERROR: Int = 0x8002044d.toInt()
    val ERROR_UTILITY_INVALID_STATUS: Int = 0x80110001.toInt()
    val ERROR_UTILITY_INVALID_PARAM_ADDR: Int = 0x80110002.toInt()
    val ERROR_UTILITY_IS_UNKNOWN: Int = 0x80110003.toInt()
    val ERROR_UTILITY_INVALID_PARAM_SIZE: Int = 0x80110004.toInt()
    val ERROR_UTILITY_WRONG_TYPE: Int = 0x80110005.toInt()
    val ERROR_UTILITY_MODULE_NOT_FOUND: Int = 0x80110006.toInt()
    val ERROR_SAVEDATA_LOAD_NO_MEMSTICK: Int = 0x80110301.toInt()
    val ERROR_SAVEDATA_LOAD_MEMSTICK_REMOVED: Int = 0x80110302.toInt()
    val ERROR_SAVEDATA_LOAD_ACCESS_ERROR: Int = 0x80110305.toInt()
    val ERROR_SAVEDATA_LOAD_DATA_BROKEN: Int = 0x80110306.toInt()
    val ERROR_SAVEDATA_LOAD_NO_DATA: Int = 0x80110307.toInt()
    val ERROR_SAVEDATA_LOAD_BAD_PARAMS: Int = 0x80110308.toInt()
    val ERROR_SAVEDATA_LOAD_NO_UMD: Int = 0x80110309.toInt()
    val ERROR_SAVEDATA_LOAD_INTERNAL_ERROR: Int = 0x80110309.toInt()
    val ERROR_SAVEDATA_RW_NO_MEMSTICK: Int = 0x80110321.toInt()
    val ERROR_SAVEDATA_RW_MEMSTICK_REMOVED: Int = 0x80110322.toInt()
    val ERROR_SAVEDATA_RW_MEMSTICK_FULL: Int = 0x80110323.toInt()
    val ERROR_SAVEDATA_RW_MEMSTICK_PROTECTED: Int = 0x80110324.toInt()
    val ERROR_SAVEDATA_RW_ACCESS_ERROR: Int = 0x80110325.toInt()
    val ERROR_SAVEDATA_RW_DATA_BROKEN: Int = 0x80110326.toInt()
    val ERROR_SAVEDATA_RW_NO_DATA: Int = 0x80110327.toInt()
    val ERROR_SAVEDATA_RW_BAD_PARAMS: Int = 0x80110328.toInt()
    val ERROR_SAVEDATA_RW_FILE_NOT_FOUND: Int = 0x80110329.toInt()
    val ERROR_SAVEDATA_RW_CAN_NOT_SUSPEND: Int = 0x8011032a.toInt()
    val ERROR_SAVEDATA_RW_INTERNAL_ERROR: Int = 0x8011032b.toInt()
    val ERROR_SAVEDATA_RW_BAD_STATUS: Int = 0x8011032c.toInt()
    val ERROR_SAVEDATA_RW_SECURE_FILE_FULL: Int = 0x8011032d.toInt()
    val ERROR_SAVEDATA_DELETE_NO_MEMSTICK: Int = 0x80110341.toInt()
    val ERROR_SAVEDATA_DELETE_MEMSTICK_REMOVED: Int = 0x80110342.toInt()
    val ERROR_SAVEDATA_DELETE_MEMSTICK_PROTECTED: Int = 0x80110344.toInt()
    val ERROR_SAVEDATA_DELETE_ACCESS_ERROR: Int = 0x80110345.toInt()
    val ERROR_SAVEDATA_DELETE_DATA_BROKEN: Int = 0x80110346.toInt()
    val ERROR_SAVEDATA_DELETE_NO_DATA: Int = 0x80110347.toInt()
    val ERROR_SAVEDATA_DELETE_BAD_PARAMS: Int = 0x80110348.toInt()
    val ERROR_SAVEDATA_DELETE_INTERNAL_ERROR: Int = 0x8011034b.toInt()
    val ERROR_SAVEDATA_SAVE_NO_MEMSTICK: Int = 0x80110381.toInt()
    val ERROR_SAVEDATA_SAVE_MEMSTICK_REMOVED: Int = 0x80110382.toInt()
    val ERROR_SAVEDATA_SAVE_NO_SPACE: Int = 0x80110383.toInt()
    val ERROR_SAVEDATA_SAVE_MEMSTICK_PROTECTED: Int = 0x80110384.toInt()
    val ERROR_SAVEDATA_SAVE_ACCESS_ERROR: Int = 0x80110385.toInt()
    val ERROR_SAVEDATA_SAVE_BAD_PARAMS: Int = 0x80110388.toInt()
    val ERROR_SAVEDATA_SAVE_NO_UMD: Int = 0x80110389.toInt()
    val ERROR_SAVEDATA_SAVE_WRONG_UMD: Int = 0x8011038a.toInt()
    val ERROR_SAVEDATA_SAVE_INTERNAL_ERROR: Int = 0x8011038b.toInt()
    val ERROR_SAVEDATA_SIZES_NO_MEMSTICK: Int = 0x801103c1.toInt()
    val ERROR_SAVEDATA_SIZES_MEMSTICK_REMOVED: Int = 0x801103c2.toInt()
    val ERROR_SAVEDATA_SIZES_ACCESS_ERROR: Int = 0x801103c5.toInt()
    val ERROR_SAVEDATA_SIZES_DATA_BROKEN: Int = 0x801103c6.toInt()
    val ERROR_SAVEDATA_SIZES_NO_DATA: Int = 0x801103c7.toInt()
    val ERROR_SAVEDATA_SIZES_BAD_PARAMS: Int = 0x801103c8.toInt()
    val ERROR_SAVEDATA_SIZES_INTERNAL_ERROR: Int = 0x801103cb.toInt()
    val ERROR_NETPARAM_BAD_NETCONF: Int = 0x80110601.toInt()
    val ERROR_NETPARAM_BAD_PARAM: Int = 0x80110604.toInt()
    val ERROR_NET_MODULE_BAD_ID: Int = 0x80110801.toInt()
    val ERROR_NET_MODULE_ALREADY_LOADED: Int = 0x80110802.toInt()
    val ERROR_NET_MODULE_NOT_LOADED: Int = 0x80110803.toInt()
    val ERROR_AV_MODULE_BAD_ID: Int = 0x80110901.toInt()
    val ERROR_AV_MODULE_ALREADY_LOADED: Int = 0x80110902.toInt()
    val ERROR_AV_MODULE_NOT_LOADED: Int = 0x80110903.toInt()
    val ERROR_MODULE_BAD_ID: Int = 0x80111101.toInt()
    val ERROR_MODULE_ALREADY_LOADED: Int = 0x80111102.toInt()
    val ERROR_MODULE_NOT_LOADED: Int = 0x80111103.toInt()
    val ERROR_SCREENSHOT_CONT_MODE_NOT_INIT: Int = 0x80111229.toInt()
    val ERROR_UMD_NOT_READY: Int = 0x80210001.toInt()
    val ERROR_UMD_LBA_OUT_OF_BOUNDS: Int = 0x80210002.toInt()
    val ERROR_UMD_NO_DISC: Int = 0x80210003.toInt()
    val ERROR_MEMSTICK_DEVCTL_BAD_PARAMS: Int = 0x80220081.toInt()
    val ERROR_MEMSTICK_DEVCTL_TOO_MANY_CALLBACKS: Int = 0x80220082.toInt()
    val ERROR_AUDIO_CHANNEL_NOT_INIT: Int = 0x80260001.toInt()
    val ERROR_AUDIO_CHANNEL_BUSY: Int = 0x80260002.toInt()
    val ERROR_AUDIO_INVALID_CHANNEL: Int = 0x80260003.toInt()
    val ERROR_AUDIO_PRIV_REQUIRED: Int = 0x80260004.toInt()
    val ERROR_AUDIO_NO_CHANNELS_AVAILABLE: Int = 0x80260005.toInt()
    val ERROR_AUDIO_OUTPUT_SAMPLE_DATA_SIZE_NOT_ALIGNED: Int = 0x80260006.toInt()
    val ERROR_AUDIO_INVALID_FORMAT: Int = 0x80260007.toInt()
    val ERROR_AUDIO_CHANNEL_NOT_RESERVED: Int = 0x80260008.toInt()
    val ERROR_AUDIO_NOT_OUTPUT: Int = 0x80260009.toInt()
    val ERROR_POWER_VMEM_IN_USE: Int = 0x802b0200.toInt()
    val ERROR_NET_RESOLVER_BAD_ID: Int = 0x80410408.toInt()
    val ERROR_NET_RESOLVER_ALREADY_STOPPED: Int = 0x8041040a.toInt()
    val ERROR_NET_RESOLVER_INVALID_HOST: Int = 0x80410414.toInt()
    val ERROR_WLAN_BAD_PARAMS: Int = 0x80410d13.toInt()
    val ERROR_HTTP_NOT_INIT: Int = 0x80431001.toInt()
    val ERROR_HTTP_ALREADY_INIT: Int = 0x80431020.toInt()
    val ERROR_HTTP_NO_MEMORY: Int = 0x80431077.toInt()
    val ERROR_HTTP_SYSTEM_COOKIE_NOT_LOADED: Int = 0x80431078.toInt()
    val ERROR_HTTP_INVALID_PARAMETER: Int = 0x804311FE.toInt()
    val ERROR_SSL_NOT_INIT: Int = 0x80435001.toInt()
    val ERROR_SSL_ALREADY_INIT: Int = 0x80435020.toInt()
    val ERROR_SSL_OUT_OF_MEMORY: Int = 0x80435022.toInt()
    val ERROR_HTTPS_CERT_ERROR: Int = 0x80435060.toInt()
    val ERROR_HTTPS_HANDSHAKE_ERROR: Int = 0x80435061.toInt()
    val ERROR_HTTPS_IO_ERROR: Int = 0x80435062.toInt()
    val ERROR_HTTPS_INTERNAL_ERROR: Int = 0x80435063.toInt()
    val ERROR_HTTPS_PROXY_ERROR: Int = 0x80435064.toInt()
    val ERROR_SSL_INVALID_PARAMETER: Int = 0x804351FE.toInt()
    val ERROR_WAVE_NOT_INIT: Int = 0x80440001.toInt()
    val ERROR_WAVE_FAILED_EXIT: Int = 0x80440002.toInt()
    val ERROR_WAVE_BAD_VOL: Int = 0x8044000a.toInt()
    val ERROR_WAVE_INVALID_CHANNEL: Int = 0x80440010.toInt()
    val ERROR_WAVE_INVALID_SAMPLE_COUNT: Int = 0x80440011.toInt()
    val ERROR_FONT_INVALID_LIBID: Int = 0x80460002.toInt()
    val ERROR_FONT_INVALID_PARAMETER: Int = 0x80460003.toInt()
    val ERROR_FONT_TOO_MANY_OPEN_FONTS: Int = 0x80460009.toInt()
    val ERROR_MPEG_BAD_VERSION: Int = 0x80610002.toInt()
    val ERROR_MPEG_NO_MEMORY: Int = 0x80610022.toInt()
    val ERROR_MPEG_INVALID_ADDR: Int = 0x80610103.toInt()
    val ERROR_MPEG_INVALID_VALUE: Int = 0x806101fe.toInt()
    val ERROR_PSMF_NOT_INITIALIZED: Int = 0x80615001.toInt()
    val ERROR_PSMF_BAD_VERSION: Int = 0x80615002.toInt()
    val ERROR_PSMF_NOT_FOUND: Int = 0x80615025.toInt()
    val ERROR_PSMF_INVALID_ID: Int = 0x80615100.toInt()
    val ERROR_PSMF_INVALID_VALUE: Int = 0x806151fe.toInt()
    val ERROR_PSMF_INVALID_TIMESTAMP: Int = 0x80615500.toInt()
    val ERROR_PSMF_INVALID_PSMF: Int = 0x80615501.toInt()
    val ERROR_PSMFPLAYER_NOT_INITIALIZED: Int = 0x80616001.toInt()
    val ERROR_PSMFPLAYER_NO_MORE_DATA: Int = 0x8061600c.toInt()
    val ERROR_MPEG_NO_DATA: Int = 0x80618001.toInt()
    val ERROR_AVC_VIDEO_FATAL: Int = 0x80628002.toInt()
    val ERROR_ATRAC_NO_ID: Int = 0x80630003.toInt()
    val ERROR_ATRAC_INVALID_CODEC: Int = 0x80630004.toInt()
    val ERROR_ATRAC_BAD_ID: Int = 0x80630005.toInt()
    val ERROR_ATRAC_ALL_DATA_LOADED: Int = 0x80630009.toInt()
    val ERROR_ATRAC_NO_DATA: Int = 0x80630010.toInt()
    val ERROR_ATRAC_SECOND_BUFFER_NEEDED: Int = 0x80630012.toInt()
    val ERROR_ATRAC_SECOND_BUFFER_NOT_NEEDED: Int = 0x80630022.toInt()
    val ERROR_ATRAC_BUFFER_IS_EMPTY: Int = 0x80630023.toInt()
    val ERROR_ATRAC_ALL_DATA_DECODED: Int = 0x80630024.toInt()
    val ERROR_CODEC_AUDIO_FATAL: Int = 0x807f00fc.toInt()
    val FATAL_UMD_UNKNOWN_MEDIUM: Int = 0xC0210004.toInt()
    val FATAL_UMD_HARDWARE_FAILURE: Int = 0xC0210005.toInt()
    val ERROR_AUDIO_INVALID_FREQUENCY: Int = 0x8026000A.toInt()
    val ERROR_AUDIO_INVALID_VOLUME: Int = 0x8026000B.toInt()
    val ERROR_AUDIO_CHANNEL_ALREADY_RESERVED: Int = 0x80268002.toInt()
    val PSP_AUDIO_ERROR_SRC_FORMAT_4: Int = 0x80000003.toInt()
    val ATRAC_ERROR_API_FAIL: Int = 0x80630002.toInt()
    val ATRAC_ERROR_NO_ATRACID: Int = 0x80630003.toInt()
    val ATRAC_ERROR_INVALID_CODECTYPE: Int = 0x80630004.toInt()
    val ATRAC_ERROR_BAD_ATRACID: Int = 0x80630005.toInt()
    val ATRAC_ERROR_ALL_DATA_LOADED: Int = 0x80630009.toInt()
    val ATRAC_ERROR_NO_DATA: Int = 0x80630010.toInt()
    val ATRAC_ERROR_SECOND_BUFFER_NEEDED: Int = 0x80630012.toInt()
    val ATRAC_ERROR_INCORRECT_READ_SIZE: Int = 0x80630013.toInt()
    val ATRAC_ERROR_ADD_DATA_IS_TOO_BIG: Int = 0x80630018.toInt()
    val ATRAC_ERROR_UNSET_PARAM: Int = 0x80630021.toInt()
    val ATRAC_ERROR_SECOND_BUFFER_NOT_NEEDED: Int = 0x80630022.toInt()
    val ATRAC_ERROR_BUFFER_IS_EMPTY: Int = 0x80630023.toInt()
    val ATRAC_ERROR_ALL_DATA_DECODED: Int = 0x80630024.toInt()
    val PSP_SYSTEMPARAM_RETVAL: Int = 0x80110103.toInt()
    val ERROR_SAS_INVALID_VOICE: Int = 0x80420010.toInt()
    val ERROR_SAS_INVALID_ADSR_CURVE_MODE: Int = 0x80420013.toInt()
    val ERROR_SAS_INVALID_PARAMETER: Int = 0x80420014.toInt()
    val ERROR_SAS_INVALID_LOOP_POS: Int = 0x80420015.toInt()
    val ERROR_SAS_VOICE_PAUSED: Int = 0x80420016.toInt()
    val ERROR_SAS_BUSY: Int = 0x80420030.toInt()
    val ERROR_SAS_NOT_INIT: Int = 0x80420100.toInt()
    val ERROR_SAS_INVALID_GRAIN: Int = 0x80420001.toInt()
    val ERROR_SAS_INVALID_MAX_VOICES: Int = 0x80420002.toInt()
    val ERROR_SAS_INVALID_OUTPUT_MODE: Int = 0x80420003.toInt()
    val ERROR_SAS_INVALID_SAMPLE_RATE: Int = 0x80420004.toInt()
    val ERROR_SAS_INVALID_ADDRESS: Int = 0x80420005.toInt()
    val ERROR_SAS_INVALID_VOICE_INDEX: Int = 0x80420010.toInt()
    val ERROR_SAS_INVALID_NOISE_CLOCK: Int = 0x80420011.toInt()
    val ERROR_SAS_INVALID_PITCH_VAL: Int = 0x80420012.toInt()
    val ERROR_SAS_INVALID_ADPCM_SIZE: Int = 0x80420014.toInt()
    val ERROR_SAS_INVALID_LOOP_MODE: Int = 0x80420015.toInt()
    val ERROR_SAS_INVALID_VOLUME_VAL: Int = 0x80420018.toInt()
    val ERROR_SAS_INVALID_ADSR_VAL: Int = 0x80420019.toInt()
    val ERROR_SAS_INVALID_SIZE: Int = 0x8042001A.toInt()
    val ERROR_SAS_INVALID_FX_TYPE: Int = 0x80420020.toInt()
    val ERROR_SAS_INVALID_FX_FEEDBACK: Int = 0x80420021.toInt()
    val ERROR_SAS_INVALID_FX_DELAY: Int = 0x80420022.toInt()
    val ERROR_SAS_INVALID_FX_VOLUME_VAL: Int = 0x80420023.toInt()
    val ERROR_SAS_ALREADY_INIT: Int = 0x80420101.toInt()
    val PSP_POWER_ERROR_TAKEN_SLOT: Int = 0x80000020.toInt()
    val PSP_POWER_ERROR_SLOTS_FULL: Int = 0x80000022.toInt()
    val PSP_POWER_ERROR_PRIVATE_SLOT: Int = 0x80000023.toInt()
    val PSP_POWER_ERROR_EMPTY_SLOT: Int = 0x80000025.toInt()
    val PSP_POWER_ERROR_INVALID_CB: Int = 0x80000100.toInt()
    val PSP_POWER_ERROR_INVALID_SLOT: Int = 0x80000102.toInt()
}
