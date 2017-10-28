package com.soywiz.kpspemu.cpu.dis

import com.soywiz.korio.lang.format
import com.soywiz.kpspemu.cpu.InstructionDecoder
import com.soywiz.kpspemu.cpu.InstructionOpcodeDecoder
import com.soywiz.kpspemu.cpu.InstructionType
import com.soywiz.kpspemu.mem.Memory

object Disassembler : InstructionDecoder {
	fun gprStr(i: Int) = "r$i"

	fun disasmMacro(pc: Int, i: Int): String {
		val op = InstructionOpcodeDecoder(i)
		if (i == 0) return "nop"
		return disasm(op, pc, i)
	}

	fun disasm(pc: Int, i: Int): String = disasm(InstructionOpcodeDecoder(i), pc, i)

	fun disasm(op: InstructionType, pc: Int, i: Int): String {
		val params = op.format.replace(Regex("%\\w+")) {
			val type = it.groupValues[0]
			when (type) {
				"%d" -> gprStr(i.rd)
				"%s" -> gprStr(i.rs)
				"%a" -> "${i.pos}"
				"%t" -> gprStr(i.rt)
				"%C" -> "0x%04X".format(i.syscall)
				"%I" -> "${i.u_imm16}"
				"%i" -> "${i.s_imm16}"
				else -> type
			}
		}

		return "${op.name} $params"
	}
}

fun Memory.disasm(pc: Int): String = Disassembler.disasm(pc, this.lw(pc))
