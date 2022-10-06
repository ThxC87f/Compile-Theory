package org.compiler.exp01.api

typealias Token = Pair<Int, String>

interface Tokenizer {
	
	fun parseToken(text: String): MutableList<Token>
	
}
