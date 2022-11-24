package org.compiler.exp01.common

private val preProcessor = CompilePreProcessor()

fun readText(filename: String, isTxt: Boolean = true, preProcess: Boolean = true): String {
	val fn = if (isTxt && !filename.endsWith(".txt")) "${filename}.txt" else filename
	val url = ClassLoader.getSystemResource(fn)
		?: throw CompileException("filename is not found in classpath")
	return url.readText().let { if (!preProcess) it else preProcessor.preProcess(it) }
}
