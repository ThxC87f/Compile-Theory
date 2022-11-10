package org.compiler.exp02

import org.compiler.exp01.api.impl.Exp1Tokenizer
import org.compiler.exp01.common.readText
import org.compiler.exp02.api.impl.Exp2Parser

fun main() {
	val text = readText("exp02", preProcess = true)
	val tokenizer = Exp1Tokenizer()
	val parser = Exp2Parser()
	val tokens = tokenizer.parseToken(text)
	println(text)
	println(tokens)
	println(parser.judgeText(tokens))
}
