package org.compiler.exp02.api

import org.compiler.exp01.api.Token


interface RecursiveDescentParser {
	
	fun judgeText(tokens: List<Token>): Boolean
	
}
