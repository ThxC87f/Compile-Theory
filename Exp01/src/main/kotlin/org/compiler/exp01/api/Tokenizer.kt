package org.compiler.exp01.api

typealias Token = Pair<Int, String>

interface Tokenizer {
	
	fun parseToken(text: String): MutableList<Token>
	
	fun isIdentifierChar(c: Char): Boolean {
		return c in 'a'..'z' ||
				c in 'A'..'Z' ||
				c in '0'..'9' ||
				c == '_'
	}
}
