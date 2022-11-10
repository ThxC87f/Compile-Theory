package org.compiler.exp01.common

class TokenRegister {
	companion object {
		val separatorWordList = listOf("{", "}", "(", ")", ";", ",")
		val reservedWordList = listOf("if", "else", "while", "do", "for", "int")
		val operatorWordList = listOf("+", "-", "*", "/", "**", "=", "==", "!=", ">", "<", ">=", "<=", "|", "&")
		const val BLANK_CHAR = " "
		const val DIGIT_TYPE_CODE = 1
		const val IDENTIFIER_TYPE_CODE = 2
		const val START_AUTO_GENERATOR_TYPE_CODE = 30
		
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
		
		fun getTypeCode(word: String): Int {
			return wordTypeCodeMap[word] ?: throw CompileException("'$word' has no type code")
		}
		
		fun getCodeExplanation(code: Int): String {
			return codeExplanationMap[code]!!
		}
	}
}
