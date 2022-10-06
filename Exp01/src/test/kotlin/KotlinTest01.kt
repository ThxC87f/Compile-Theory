import org.compiler.exp01.api.impl.TokenizerImpl
import org.compiler.exp01.common.CompilePreProcessor
import kotlin.test.Test
import kotlin.test.asserter

class KotlinTest01 {
	
	val tokenizer = TokenizerImpl()
	val processor = CompilePreProcessor()
	
	val code = ClassLoader.getSystemResource("code.txt").readText()
	
	@Test
	fun test_StringIsDigit() {
		asserter.assertTrue("10 is digit", "10".matches(tokenizer.digitRegex))
		asserter.assertTrue("main is not digit", !"main".matches(tokenizer.digitRegex))
		
	}
	
	@Test
	fun test_PreProcess() {
		println(processor.preProcess(code))
	}
	
	@Test
	fun test_GetTypeCode() {
		println(tokenizer.getTypeCode("if"))
		println(tokenizer.getTypeCode("+"))
		println(tokenizer.getTypeCode(";"))
		println(tokenizer.getTypeCode(">="))
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
			printer(it.first.toString(), tokenizer.getCodeExplanation(it.first), it.second)
		}
	}
	
}