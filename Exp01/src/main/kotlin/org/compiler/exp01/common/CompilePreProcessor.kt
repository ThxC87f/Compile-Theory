package org.compiler.exp01.common

import java.util.*

open class CompilePreProcessor {
	
	var stringQuotes: List<Char> = listOf('\'', '\"')
	var unusedCharList = listOf(' ', '\t', '\n', '\r')
	
	/**
	 * 预处理
	 * 作用：去掉文本中无用的符号
	 *
	 * 无用符号包括：多个空格，\n，\t
	 */
	fun preProcess(text: String): String {
		val list = LinkedList<Char>()
		
		var cur = 0
		var inString = false
		
		while (cur < text.length) {
			val it = text[cur++]
			when (it) {
				// 是无效字符
				in unusedCharList -> {
					if (list.peekLast() != null && list.last != ' ') {
						list.addLast(' ')
					}
					continue
				}
				// 是字符串引号
				in stringQuotes -> {
					inString = true
				}
				// 疑似注释
				'/' -> {
					if (!inString) {
						// 确实是注释
						cur = walkComment(text, cur)
						continue
					}
				}
			}
			
			list.addLast(it)
		}
		
		val builder = StringBuilder(list.size)
		while (list.isNotEmpty()) {
			builder.append(list.pollFirst())
		}
		
		return builder.toString()
	}
	
	fun walkComment(text: String, current: Int): Int {
		var cur = current
		val stopChar = if (text[cur++] == '/') '\n' else '*'
		
		@Suppress("ControlFlowWithEmptyBody")
		while (cur < text.length && text[cur++] != stopChar) {
		}
		
		if (stopChar == '\n') {
			return cur
		}
		
		if (cur != text.length) {
			var it = text[cur++]
			while (it != '/' && cur < text.length) {
				it = text[cur++]
			}
			
			if (it == '/') {
				return cur
			}
		}
		
		throw CompileException("多行注释没有闭合")
	}
	
	
}

