package org.compiler.exp01.api.impl

import org.compiler.exp01.api.Token
import org.compiler.exp01.api.Tokenizer
import org.compiler.exp01.common.TokenizerException

class TokenizerImpl : Tokenizer {
	
	val wordTypeCodeMap: Map<String, Int>
	val codeExplanationMap: Map<Int, String>
	val separatorWordList = listOf("{", "}", "(", ")", ";", ",")
	val reservedWordList = listOf("if", "else", "while", "do", "for", "int")
	val operatorWordList = listOf("+", "-", "*", "/", "**", "=", "==", "!=", ">", "<", ">=", "<=", "|", "&")
	val digitTypeCode = 1
	val identifierTypeCode = 2
	val startAutoGenerateTypeCode = 30
	
	init {
		wordTypeCodeMap = HashMap()
		var code = startAutoGenerateTypeCode
		separatorWordList.forEach { wordTypeCodeMap[it] = code++ }
		reservedWordList.forEach { wordTypeCodeMap[it] = code++ }
		operatorWordList.forEach { wordTypeCodeMap[it] = code++ }
		
		codeExplanationMap = HashMap()
		wordTypeCodeMap.forEach { (k, v) -> codeExplanationMap[v] = k }
	}
	
	override fun parseToken(text: String): MutableList<Token> {
		// main() { int a,b; a = 10; b = a + 20; }
		val tokens = ArrayList<Token>()
		
		var it: String
		var cur = 0
		
		while (cur < text.length) {
			it = text[cur].toString()
			when (it) {
				" " -> {}
				
				in separatorWordList -> {
					tokens.add(Pair(getTypeCode(it), it))
				}
				
				in operatorWordList -> {
					val (word, _cur) = walkOneWord(text, cur) {
						it.toString() in operatorWordList
					}
					tokens.add(Pair(getTypeCode(word), word))
					cur = _cur
					continue
				}
				
				else -> {
					// [保留字]，[标识符]，[常数]，[操作符]
					val (word, _cur) = walkOneWord(text, cur, ::isIdentifierChar)
					
					if (cur == _cur) {
						throw TokenizerException("无法识别字符'${text[cur]}'，位于第${cur}个字符，文本为${text}")
					}
					
					when (word) {
						in reservedWordList -> {
							tokens.add(Pair(getTypeCode(word), word))
						}
						
						else -> {
							tokens.add(Pair(if (word.isDigit()) digitTypeCode else identifierTypeCode, word))
						}
					}
					
					cur = _cur
					continue
				}
			}
			cur++
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
		var flag = codeExplanationMap[code]
		if (flag == null) {
			flag = if (code == identifierTypeCode) "_ID_" else "_CONST_"
		}
		
		return flag
	}
	
	val digitRegex = Regex("^([-+])?\\d+(\\.\\d+)?\$")
	
	// -10.5
	fun String.isDigit(): Boolean {
		return this.matches(digitRegex)
	}
	
}

