import org.compiler.exp01.api.impl.Exp1Tokenizer
import org.compiler.exp04.SemanticTranslator
import org.junit.Test
import kotlin.test.assertEquals

class Exp4Tests {

	@Test
	fun test_case() {
		val translator = SemanticTranslator()
		val exp1Tokenizer = Exp1Tokenizer()

		val caseAndAns: Map<String, Number> = mapOf(
			"10*20-30" to 170,
			"10+(20-5)/5" to 13,
		)

		caseAndAns.forEach { (code, ans) ->
			val tokens = exp1Tokenizer.parseToken(code)
			val result = translator.translateStatement(tokens)
			println(result)
			assertEquals(ans.toDouble(), result.toDouble())
		}

	}

}