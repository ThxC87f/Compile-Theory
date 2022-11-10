package org.compiler.exp01.common

private val preProcessor = CompilePreProcessor()

fun readText(filename: String, isTxt: Boolean = true, preProcess: Boolean = true): String {
	val url = ClassLoader.getSystemResource(if (isTxt) "${filename}.txt" else filename)
		?: throw CompileException("filename is not found in classpath")
	return url.readText().let { if (!preProcess) it else preProcessor.preProcess(it) }
}
