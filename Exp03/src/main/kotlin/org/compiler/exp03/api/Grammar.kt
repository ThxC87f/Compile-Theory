package org.compiler.exp03.api

import org.compiler.exp01.common.CompileException

class Grammar(val text: String, val beginSymbol: Char = 'S') {

	// 非终结符集
	val nonTerminal = mutableSetOf<Char>()

	// 终结符集
	val terminal = mutableSetOf<Char>()

	// LL1 预测分析表
	lateinit var ll1Table: LL1Table
	lateinit var ll1TableIndex: Map<Char, Int>


	fun ofMapping(nonTerminalChar: Char,
				  terminalChar: Char): String? {
		val terminalList = ll1Table[nonTerminalChar]
			?: throw CompileException("非终结符 '$nonTerminalChar' 不存在于本文法的预测分析表中：${this.ll1Table}")
		val index = ll1TableIndex[terminalChar]
			?: throw CompileException("终结符 '$terminalChar' 不存在于本文法的预测分析表中：${this.ll1TableIndex}")
		return terminalList[index]
	}

}