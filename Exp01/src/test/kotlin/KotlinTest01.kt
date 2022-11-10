import org.compiler.exp01.api.impl.Exp1Tokenizer
import org.compiler.exp01.common.CompilePreProcessor
import org.compiler.exp01.common.TokenRegister
import org.compiler.exp01.common.readText
import org.junit.Assert
import org.junit.Test

class KotlinTest01 {
	
	val tokenizer = Exp1Tokenizer()
	val processor = CompilePreProcessor()
	val code = readText("exp01.txt")
	
	@Test
	fun test_StringIsDigit() {
		Assert.assertTrue("10 is digit", "10".matches(tokenizer.digitRegex))
		Assert.assertTrue("main is not digit", !"main".matches(tokenizer.digitRegex))
	}
	
	@Test
	fun test_PreProcess() {
		val emptyCodeText = listOf(
			"",
			"//",
			"/**/",
			"/***/",
			"/****/",
			"/*123*/",
			"/*1234*/",
			"/*abed*123/*****1234/*2*2*2*2*2*2*2*/"
		)
		
		val exceptionCodeText = mapOf(
			"/" to "非法注释",
			"/**" to "多行注释未闭合",
			"/*" to "非法注释"
		)
		
		emptyCodeText.forEach {
			Assert.assertEquals(processor.preProcess(it), "")
		}
		
		exceptionCodeText.forEach { (t, u) ->
			try {
				processor.preProcess(t)
			} catch (e: Exception) {
				Assert.assertEquals(e.message, u)
			}
		}
		
		println("空注释和异常注释的测试全部通过！！")
		println(processor.preProcess(code))
	}
	
	@Test
	fun test_GetTypeCode() {
		println(TokenRegister.getTypeCode("if"))
		println(TokenRegister.getTypeCode("+"))
		println(TokenRegister.getTypeCode(";"))
		println(TokenRegister.getTypeCode(">="))
	}
	
	@Test
	fun test_Tokenize() {
		fun printer(s1: String, s2: String, s3: String) {
			println(String.format("%-10s\t\t%-10s\t\t%s", s1, s2, s3))
		}
		
		val tokens = tokenizer.parseToken(processor.preProcess(code))
		
		// pretty print
		printer("[Code]", "[Type]", "[Value]")
		tokens.forEach {
			printer(it.code.toString(), TokenRegister.getCodeExplanation(it.code), it.value)
		}
	}
	
}