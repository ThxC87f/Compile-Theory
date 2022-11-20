package org.compiler.exp02

import org.compiler.exp01.api.impl.Exp1Tokenizer
import org.compiler.exp01.common.readText
import org.compiler.exp02.api.impl.Exp2Parser

fun main() {
	val text = readText("exp02", preProcess = true)
	val tokenizer = Exp1Tokenizer()
	val parser = Exp2Parser()
	val tokens = tokenizer.parseToken(text)
	println("预处理后的源代码：$text")
	println("使用Exp1的Tokenizer解析得到的token列表：$tokens")
	println("是否满足实验二所期望的文法：${parser.judgeText(tokens)}")
}
