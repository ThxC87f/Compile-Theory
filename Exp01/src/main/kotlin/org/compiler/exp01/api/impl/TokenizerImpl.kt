package org.compiler.exp01.api.impl

import org.compiler.exp01.api.Token
import org.compiler.exp01.api.Tokenizer
import org.compiler.exp01.common.TokenizerException

class TokenizerImpl : Tokenizer {
	
	companion object {
		val separatorWordList = listOf("{", "}", "(", ")", ";", ",")
		val reservedWordList = listOf("if", "else", "while", "do", "for", "int")
		val operatorWordList = listOf("+", "-", "*", "/", "**", "=", "==", "!=", ">", "<", ">=", "<=", "|", "&")
		const val BLANK_CHAR = " "
		const val DIGIT_TYPE_CODE = 1
		const val IDENTIFIER_TYPE_CODE = 2
		const val START_AUTO_GENERATOR_TYPE_CODE = 30
	}
	
	val wordTypeCodeMap: Map<String, Int>
	val codeExplanationMap: Map<Int, String>
	
	
	init {
		wordTypeCodeMap = HashMap()
		var code = START_AUTO_GENERATOR_TYPE_CODE
		separatorWordList.forEach { wordTypeCodeMap[it] = code++ }
		reservedWordList.forEach { wordTypeCodeMap[it] = code++ }
		operatorWordList.forEach { wordTypeCodeMap[it] = code++ }
		
		codeExplanationMap = HashMap()
		wordTypeCodeMap.forEach { (k, v) -> codeExplanationMap[v] = k }
		codeExplanationMap[IDENTIFIER_TYPE_CODE] = "_ID_"
		codeExplanationMap[DIGIT_TYPE_CODE] = "_CONST_"
	}
	
	override fun parseToken(text: String): MutableList<Token> {
		// main() { int a,b; a = 10; b = a + 20; }
		val tokens = ArrayList<Token>()
		
		var it: String
		var cur = 0
		
		while (cur < text.length) {
			it = text[cur].toString()
			when (it) {
				BLANK_CHAR -> {
					cur++
				}
				
				in separatorWordList -> {
					tokens.add(Pair(getTypeCode(it), it))
					cur++
				}
				
				in operatorWordList -> {
					val (word, _cur) = walkOneWord(text, cur) {
						it.toString() in operatorWordList
					}
					tokens.add(Pair(getTypeCode(word), word))
					cur = _cur
				}
				
				else -> {
					// [保留字]，[标识符]，[常数]，[操作符]
					val (word, _cur) = walkOneWord(text, cur, ::isIdentifierChar)
					
					if (cur == _cur) {
						throw TokenizerException("无法识别字符'${text[cur]}'，位于第${cur}个字符，文本为${text}")
					}
					
					cur = _cur
					when (word) {
						in reservedWordList -> {
							tokens.add(Pair(getTypeCode(word), word))
						}
						
						else -> {
							tokens.add(Pair(if (word.isDigit()) DIGIT_TYPE_CODE else IDENTIFIER_TYPE_CODE, word))
						}
					}
				}
			}
			
		}
		
		return tokens
	}
	
	fun walkOneWord(text: String, begin: Int, predicate: (Char) -> Boolean): Pair<String, Int> {
		val builder = StringBuilder()
		var cur = begin
		var c = text[cur]
		while (predicate(c)) {
			builder.append(c)
			cur++
			if (cur >= text.length) {
				break
			}
			
			c = text[cur]
		}
		return Pair(builder.toString(), cur)
	}
	
	fun getTypeCode(word: String): Int {
		return wordTypeCodeMap[word]!!
	}
	
	fun getCodeExplanation(code: Int): String {
		return codeExplanationMap[code]!!
	}
	
	val digitRegex = Regex("^([-+])?\\d+(\\.\\d+)?\$")
	
	fun String.isDigit(): Boolean {
		return this.matches(digitRegex)
	}
	
	fun isIdentifierChar(c: Char): Boolean {
		return c in 'a'..'z' ||
				c in 'A'..'Z' ||
				c in '0'..'9' ||
				c == '_'
	}
	
}

