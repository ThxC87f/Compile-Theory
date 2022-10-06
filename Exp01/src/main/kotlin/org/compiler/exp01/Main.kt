package org.compiler.exp01

import org.compiler.exp01.api.impl.TokenizerImpl
import org.compiler.exp01.common.CompilePreProcessor

fun main() {
	val tokenizer = TokenizerImpl()
	val preProcessor = CompilePreProcessor()
	
	// 读取代码文本
	var code = ClassLoader.getSystemResource("code.txt").readText()
	// 预处理
	code = preProcessor.preProcess(code)
	// 将代码文本解析为Token列表
	val tokenList = tokenizer.parseToken(code)
	// 打印结果
	tokenList.forEach { println(it) }
}