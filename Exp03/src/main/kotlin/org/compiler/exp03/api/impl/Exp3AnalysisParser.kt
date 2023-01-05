package org.compiler.exp03.api.impl

import org.compiler.exp03.api.Grammar
import org.compiler.exp03.api.PredictiveAnalysisParser
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
class Exp3AnalysisParser : PredictiveAnalysisParser {

	companion object {
		const val size = 24
		var firstRun = true
	}

	override fun isLL1Grammar(source: String, grammar: Grammar): Boolean {
		val text = "$source#"
		// pointer of text
		var cur = 0
		val stack = ArrayDeque<Char>()
		stack.push('#')
		stack.push(grammar.beginSymbol)

		// 打印信息
		tipAnalysisStart()

		while (true) {
			val noTerChar = stack.peek()
			val terChar = text[cur]

			// 分析栈已空，分析结束
			if (noTerChar == '#' && terChar == '#') {
				tipAnalysisProcedure(stack, text.substring(cur), "分析结束")
				return printMsgAndReturnTrue { "句子 \"${source}\" 分析成功" }
			}

			// 都是终结符
			if (noTerChar == terChar) {
				tipAnalysisProcedure(stack, text.substring(cur), "$noTerChar == $terChar")
				stack.pop()
				cur++
				continue
			}

			// grammarIt 是非终结符，去表中查询
			val tableResult = grammar.ofMapping(noTerChar, terChar)
			if (tableResult == null) {
				tipAnalysisProcedure(stack, text.substring(cur), "$noTerChar !-> $terChar")
				return printMsgAndReturnFalse {
					"句子 \"$source\" 分析失败，无法匹配非终结符 '$noTerChar' 与终结符 '$terChar'。" +
							"$noTerChar 的预测分析表：${grammar.ll1Table[noTerChar]}"
				}
			}

			tipAnalysisProcedure(stack, text.substring(cur), "$noTerChar ${PredictiveAnalysisParser.SEPARATOR} $tableResult")

			// 弹出当前符号
			stack.pop()
			if (tableResult == PredictiveAnalysisParser.NONE) {
				continue
			}

			// 压入符号
			for (i in tableResult.length - 1 downTo 0) {
				stack.push(tableResult[i])
			}

		}
	}

	private fun tipAnalysisStart() {
		if (firstRun) {
			tipAnalysisProcedure("分析栈", "字符串", "产生式")
			separationLine()
			firstRun = true
		}
	}

	fun tipAnalysisProcedure(s1: Any, s2: Any, s3: Any, padChar: Char = ' ') {

		fun Any.padEnd(): String {
			return this.toString().padEnd(size, padChar)
		}

		@Suppress("NAME_SHADOWING")
		val s1 = if (s1 is ArrayDeque<*>) {
			s1.reversed()
		} else {
			s1
		}

		println("${s1.padEnd()}\t${s2.padEnd()}\t${s3.padEnd()}")
	}

	inline fun printMsgAndReturnFalse(messageSupplier: () -> String): Boolean {
		println(messageSupplier())
		separationLine()
		return false
	}

	inline fun printMsgAndReturnTrue(msgSupplier: () -> String): Boolean {
		println(msgSupplier())
		separationLine()
		return true
	}

	fun separationLine() {
		println("-".padEnd(3 * size, '-'))
	}

}