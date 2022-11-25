package org.compiler.exp03.api

typealias LL1Table = Map<Char, List<String?>>

/**
（1）消除左递归
（2）计算非终结符的FIRST和FOLLOW集
（3）构造预测分析表
（4）判断消除了左递归后的文法是否为LL(1)文法
（5）编写预测分析法语法分析程序，要求对输入的任意符号串进行语法分析，输出推导过程或语法分析树。
 */
interface PredictiveAnalysisParser {

	companion object {
		const val NONE = "ε"
		const val SEPARATOR = "->"
	}

	/**
	 * text: "E::=E + T"
	 * ret:  "E" ---> "E+T"
	 */
	fun ofGrammarRule(text: String): Grammar {
		return Grammar(text)
	}

	/**
	 * 求 first 集
	 */
	fun ofFirstSet(grammar: Grammar): Map<Char, Set<Char>> {
		return emptyMap()
	}

	/**
	 * 求 follow 集
	 */
	fun ofFollowSet(grammar: Grammar): Map<Char, Set<Char>> {
		return emptyMap()
	}

	/**
	 * 预测分析表
	 */
	fun ofPredictionAnalysisTable(grammar: Grammar): Map<Char, Map<Char, String>> {
		return emptyMap()
	}

	/**
	 * 消除左递归
	 */
	fun solveLeftRecursion(grammar: Grammar): Grammar {
		return grammar
	}

	fun isLL1Grammar(source: String, grammar: Grammar): Boolean

}