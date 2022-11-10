package org.compiler.exp01.api

import org.compiler.exp01.common.TokenRegister

data class Token(
	val value: String,
	val code: Int = TokenRegister.getTypeCode(value)
) {
	override fun hashCode(): Int {
		var result = code
		result = 31 * result + value.hashCode()
		return result
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		return code == (other as Token).code
	}
}

interface Tokenizer {
	
	fun parseToken(text: String, throwExRatherPrintWhenError: Boolean = false): MutableList<Token>
	
}
