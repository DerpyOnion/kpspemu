package com.soywiz.kpspemu.ge

import com.soywiz.korio.async.Signal
import com.soywiz.korio.util.extract
import com.soywiz.korio.util.hex
import com.soywiz.kpspemu.WithEmulator
import com.soywiz.kpspemu.callbackManager
import com.soywiz.kpspemu.mem
import com.soywiz.kpspemu.mem.Memory
import com.soywiz.kpspemu.util.ResourceItem

class GeList(val ge: Ge, override val id: Int) : ResourceItem, WithEmulator by ge {
	val logger = com.soywiz.klogger.Logger("GeList")

	var start: Int = 0
	var stall: Int = 0
	var callback: GeCallback = GeCallback(-1)
	var pspGeListArgs: Int = 0
	var PC: Int = start
	var completed: Boolean = false
	val bb = GeBatchBuilder(ge)
	var onCompleted = Signal<Unit>()
	var phase = ListSyncKind.QUEUED

	fun reset() {
		completed = false
		bb.reset()
		onCompleted = Signal<Unit>()
		phase = ListSyncKind.QUEUED
	}

	var callstackIndex = 0
	val callstack = IntArray(0x100)
	val state = ge.state
	val stateData = state.data

	val isStalled: Boolean get() = (stall != 0) && (PC >= stall)

	fun run() {
		val mem = this.mem
		PC = PC and Memory.MASK
		stall = stall and Memory.MASK
		//println("GeList[$id].run: completed=$completed, PC=${PC.hexx}, stall=${stall.hexx}")
		while (!completed && !isStalled) {
			val cPC = PC
			PC += 4
			step(cPC, mem.lw(cPC))
		}
		if (isStalled) phase = ListSyncKind.STALL_REACHED
		if (completed) {
			phase = ListSyncKind.DRAWING_DONE
			onCompleted(Unit)
		}
	}

	fun step(cPC: Int, i: Int) {
		val op: Int = i ushr 24
		val p: Int = i and 0xFFFFFF
		//logger.level = PspLogLevel.TRACE
		logger.trace { "GE: ${cPC.hex}-${stall.hex}: ${op.hex}" }
		when (op) {
			Op.PRIM -> prim(p)
			Op.BEZIER -> {
				println("BEZIER")
			}
			Op.END -> {
				//println("END")
				bb.flush()
				completed = true
			}
			Op.TFLUSH -> {
				bb.tflush()
				bb.flush()
			}
			Op.TSYNC -> bb.tsync()
			Op.NOP -> {
				//println("GE: NOP")
				Unit
			}
			Op.DUMMY -> {
				//println("GE: DUMMY")
				Unit
			}
			Op.JUMP, Op.CALL -> {
				if (op == Op.CALL) {
					callstack[callstackIndex++] = PC
					callstack[callstackIndex++] = (state.baseOffset ushr 2)
				}
				PC = (state.baseAddress + p and 0b11.inv()) and Memory.MASK
			}
			Op.RET -> {
				TODO("RET")
			}
			Op.FINISH -> finish(p)
			Op.SIGNAL -> signal(p)
			Op.BASE, Op.IADDR, Op.VADDR, Op.OFFSETADDR -> Unit // Do not invalidate prim
			Op.PROJMATRIXDATA -> state.writeInt(Op.PROJMATRIXNUMBER, Op.MAT_PROJ, p)
			Op.VIEWMATRIXDATA -> state.writeInt(Op.VIEWMATRIXNUMBER, Op.MAT_VIEW, p)
			Op.WORLDMATRIXDATA -> state.writeInt(Op.WORLDMATRIXNUMBER, Op.MAT_WORLD, p)
			Op.BONEMATRIXDATA -> state.writeInt(Op.BONEMATRIXNUMBER, Op.MAT_BONES, p)
			Op.TGENMATRIXDATA -> state.writeInt(Op.TGENMATRIXNUMBER, Op.MAT_TEXTURE, p)

			else -> {
				if (ge.state.data[op] != p) bb.flush()
			}
		}
		stateData[op] = p
	}

	private fun prim(p: Int): PrimAction {
		val primitiveType = PrimitiveType(p.extract(16, 3))
		val vertexCount: Int = p.extract(0, 16)
		//println("PRIM: $primitiveType, $vertexCount")
		bb.setVertexKind(primitiveType, state)
		bb.addIndices(vertexCount)
		return PrimAction.FLUSH_PRIM
	}

	private fun finish(p: Int) {
		//println("FINISH")
		callbackManager.queueFunction1(callback.finish_func, callback.finish_arg)
		bb.flush()
	}

	private fun signal(p: Int) {
		//println("SIGNAL")
		callbackManager.queueFunction1(callback.signal_func, callback.signal_arg)
	}

	fun sync(syncType: Int) {
		//println("syncType:$syncType")
		run()
	}

	//fun syncAsync(syncType: Int): Promise<Unit> {
	//	//println("syncType:$syncType")
	//	val deferred = Promise.Deferred<Unit>()
	//	onCompleted.once { deferred.resolve(Unit) }
	//	return deferred.promise
	//}
}
