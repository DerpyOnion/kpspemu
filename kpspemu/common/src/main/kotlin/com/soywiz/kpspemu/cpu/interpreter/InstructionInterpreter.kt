package com.soywiz.kpspemu.cpu.interpreter

import com.soywiz.korio.lang.format
import com.soywiz.korio.util.IntEx
import com.soywiz.korio.util.udiv
import com.soywiz.korio.util.urem
import com.soywiz.kpspemu.cpu.CpuState
import com.soywiz.kpspemu.cpu.InstructionDecoder
import com.soywiz.kpspemu.cpu.InstructionEvaluator
import com.soywiz.kpspemu.cpu.InstructionType

object InstructionInterpreter : InstructionEvaluator<CpuState>(), InstructionDecoder {
	override fun unimplemented(s: CpuState, i: InstructionType): Unit = TODO("unimplemented: ${i.name} : " + i + " at ${"%08X".format(s._PC)}")

	// ALU
	override fun lui(s: CpuState) = s { RT = (U_IMM16 shl 16) }

	override fun add(s: CpuState) = s { RD = RS + RT }
	override fun addu(s: CpuState) = s { RD = RS + RT }
	override fun subu(s: CpuState) = s { RD = RS - RT }
	override fun addiu(s: CpuState) = s { RT = RS_IMM16 }
	override fun divu(s: CpuState) = s { LO = RS udiv RT; HI = RS urem RT }

	override fun mflo(s: CpuState) = s { RD = LO }
	override fun mfhi(s: CpuState) = s { RD = HI }
	override fun mfic(s: CpuState) = s { RD = IC }

	override fun mtlo(s: CpuState) = s { LO = RD }
	override fun mthi(s: CpuState) = s { HI = RD }
	override fun mtic(s: CpuState) = s { IC = RD }

	// ALU: Bit
	override fun or(s: CpuState) = s { RD = RS or RT }
	override fun xor(s: CpuState) = s { RD = RS xor RT }
	override fun and(s: CpuState) = s { RD = RS and RT }

	override fun ori(s: CpuState) = s { RT = RS or U_IMM16 }
	override fun xori(s: CpuState) = s { RT = RS xor U_IMM16 }
	override fun andi(s: CpuState) = s { RT = RS and U_IMM16 }

	override fun sll(s: CpuState) = s { RD = RT shl POS }
	override fun sra(s: CpuState) = s { RD = RT shr POS }
	override fun srl(s: CpuState) = s { RD = RT ushr POS }

	// Memory
	override fun lb(s: CpuState) = s { RT = mem.lb(RS_IMM16) }
	override fun lbu(s: CpuState) = s { RT = mem.lbu(RS_IMM16) }
	override fun lh(s: CpuState) = s { RT = mem.lh(RS_IMM16) }
	override fun lhu(s: CpuState) = s { RT = mem.lhu(RS_IMM16) }
	override fun lw(s: CpuState) = s { RT = mem.lw(RS_IMM16) }

	override fun sb(s: CpuState) = s { mem.sb(RS_IMM16, RT) }
	override fun sh(s: CpuState) = s { mem.sh(RS_IMM16, RT) }
	override fun sw(s: CpuState) = s { mem.sw(RS_IMM16, RT) }

	// Special
	override fun syscall(s: CpuState) = s { syscall(SYSCALL) }

	// Set less
	override fun slt(s: CpuState) = s { RD = if (IntEx.compare(RS, RT) < 0) 1 else 0 }

	override fun sltu(s: CpuState) = s { RD = if (IntEx.compareUnsigned(RS, RT) < 0) 1 else 0 }
	override fun sltiu(s: CpuState) = s { RD = if (IntEx.compareUnsigned(RS, S_IMM16) < 0) 1 else 0 }


	// Branch
	override fun beq(s: CpuState) = s.branch { RS == RT }
	override fun bne(s: CpuState) = s.branch { RS != RT }
	override fun bltz(s: CpuState) = s.branch { RS < 0 }
	override fun blez(s: CpuState) = s.branch { RS <= 0 }
	override fun bgtz(s: CpuState) = s.branch { RS > 0 }
	override fun bgez(s: CpuState) = s.branch { RS >= 0 }

	override fun beql(s: CpuState) = s.branchLikely { RS == RT }
	override fun bnel(s: CpuState) = s.branchLikely { RS != RT }
	override fun bltzl(s: CpuState) = s.branchLikely { RS < 0 }
	override fun blezl(s: CpuState) = s.branchLikely { RS <= 0 }
	override fun bgtzl(s: CpuState) = s.branchLikely { RS > 0 }
	override fun bgezl(s: CpuState) = s.branchLikely { RS >= 0 }

}

