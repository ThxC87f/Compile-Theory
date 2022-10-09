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
		
		while (cur < text.length) {
			when (val it = text[cur]) {
				// 是无效字符
				in unusedCharList -> {
					if (list.peekLast() != null && list.last != ' ') {
						list.addLast(' ')
					}
				}
				// 是字符串引号
				in stringQuotes -> {
					cur = walkString(text, cur) {
						list.addLast(it)
					}
					continue
				}
				// 是注释
				'/' -> {
					cur = walkComment(text, cur)
					continue
				}
				
				else -> {
					list.addLast(it)
				}
			}
			cur++
		}
		
		return list.joinToString(separator = "")
	}
	
	private fun walkString(text: String, begin: Int, accept: (Char) -> Unit): Int {
		var cur = begin
		accept(text[cur++]) // String的开始符["]
		while (text[cur] != '"') {
			when (val it = text[cur]) {
				'\\' -> {
					var lastCharIsBackslash = false
					while (text[cur] == '\\') {
						lastCharIsBackslash = if (lastCharIsBackslash) {
							accept('\\')
							false
						} else {
							true
						}
						cur++
						
						if (cur >= text.length) {
							throw CompileException("字符串引号\"没有闭合，上一个字符是[${text[cur - 1]}]")
						}
					}
					
					if (lastCharIsBackslash) {
						// "\\\t"
						accept('\\')
						accept(text[cur++])
					}
					
					continue
				}
				
				else -> {
					accept(it)
					cur++
					if (cur >= text.length) {
						throw CompileException("字符串引号\"没有闭合：[${text.substring(begin, cur - 1)}]")
					}
				}
			}
		}
		
		accept(text[cur]) // String的结束符["]
		return ++cur
	}
	
	private fun walkComment(text: String, begin: Int): Int {
		var cur = begin
		val stopChar = if (text[++cur] == '/') '\n' else '*'
		
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

