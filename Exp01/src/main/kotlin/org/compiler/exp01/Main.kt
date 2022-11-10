package org.compiler.exp01

import org.compiler.exp01.api.impl.Exp1Tokenizer
import org.compiler.exp01.common.readText

fun main() {
	val tokenizer = Exp1Tokenizer()
	// 读取代码文本
	val code = readText("exp01", preProcess = true)
	// 将代码文本解析为Token列表
	val tokenList = tokenizer.parseToken(code)
	// 打印结果
	tokenList.forEach { println(it) }
}