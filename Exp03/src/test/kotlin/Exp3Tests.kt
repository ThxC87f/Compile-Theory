import org.compiler.exp01.common.readText
import org.compiler.exp03.api.Grammar
import org.compiler.exp03.api.LL1Table
import org.compiler.exp03.api.PredictiveAnalysisParser
import org.compiler.exp03.api.impl.Exp3AnalysisParser
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class Exp3Tests {

	companion object {
		val cfgTableMapping = HashMap<Int, LL1Table>()
		val cfgTableIndex = HashMap<Int, Map<Char, Int>>()
		val parser = Exp3AnalysisParser()
	}

	@Test
	fun test_cfg_2() {
		val testCases = arrayOf("a", "(a)", "(a,a)", "(a,a,a)")

		testCases.forEach {
			assertTrue(useCfg(2, it))
		}

		assertFalse(useCfg(2, "(a,a,)"))
	}

	@Test
	fun test_cfg_1() {
		val testCases = arrayOf("$", "($)", "$*$", "$/$")

		testCases.forEach {
			assertTrue(useCfg(1, it))
		}

		assertFalse(useCfg(1, "($/)"))
	}

	private fun useCfg(i: Int, text: String): Boolean {
		val grammar = Grammar(readText("cfg$i"))
		grammar.ll1TableIndex = cfgTableIndex.of(i)
		grammar.ll1Table = cfgTableMapping.of(i)

		return parser.isLL1Grammar(text, grammar).also { println() }
	}

	init {
		@Suppress("NonAsciiCharacters", "LocalVariableName")
		val ε = PredictiveAnalysisParser.NONE
		val eil = null
		/**
		 * ---grammar---
		 * S -> TK
		 * K -> +TK | -TK | ε
		 * T -> FV
		 * V -> *FV | /FV | ε
		 * F -> (S) | $
		 *
		 * ---first---
		 * S: (, $
		 * K: +, -, ε
		 * T: (, $
		 * V: *, /, ε
		 * F: (, $
		 *
		 * ---follow---
		 * S: ), #
		 * K: ), #
		 * T: +, -, ), #
		 * V: +, -, ), #
		 * F: +, -, *, /, ), #
		 *
		 * ---index---
		 * +	-	*	/	(	)	$	#
		 * 0 	1	2	3	4	5	6	7
		 */
		cfgTableIndex[1] = mapOf('+' to 0,
								 '-' to 1,
								 '*' to 2,
								 '/' to 3,
								 '(' to 4,
								 ')' to 5,
								 '$' to 6,
								 '#' to 7)
		cfgTableMapping[1] = mapOf('S' to listOf(eil, eil, eil, eil, "TK", eil, "TK", ε),
								   'K' to listOf("+TK", "-TK", eil, eil, eil, ε, eil, ε),
								   'T' to listOf(eil, eil, eil, eil, "FV", eil, "FV", ε),
								   'V' to listOf(ε, ε, "*FV", "/FV", eil, ε, eil, ε),
								   'F' to listOf(eil, eil, eil, eil, "(S)", eil, "$", ε)
								  )

		/**
		 * ---grammar---
		 * S -> (L) | a
		 * L -> ST
		 * T -> ,ST | ε
		 *
		 * ---first---
		 * S: 	(, a
		 * L: 	(, a
		 * T:	',', ε
		 *
		 * ---follow---
		 * S:	), ',', #
		 * L:	)
		 * T:	)
		 *
		 * ---index---
		 * (	)	a	,	#
		 * 0 	1	2	3	4
		 */
		cfgTableIndex[2] = mapOf('(' to 0,
								 ')' to 1,
								 'a' to 2,
								 ',' to 3,
								 '#' to 4)

		cfgTableMapping[2] = mapOf('S' to listOf("(L)", eil, "a", eil, eil),
								   'L' to listOf("ST", eil, "ST", eil, eil),
								   'T' to listOf(eil, ε, eil, ",ST", eil))
		/**
		 * ---grammar---
		 * S -> AB
		 * A -> Da | ε
		 * B -> cC
		 * C -> aADC | ε
		 * D -> b | ε
		 *
		 * ---first---
		 * S: a,b,c
		 * A: a,b,ε
		 * B: c
		 * C: a,ε
		 * D: b,ε
		 *
		 * ---follow---
		 * S: #
		 * A: a, b, c, #
		 * B: #
		 * C: #,
		 * D: a, #
		 *
		 * ---index---
		 * 	a	b	c	#
		 * 	0	1	2	3
		 */
		/* cfgTableIndex[3] = mapOf('a' to 0,
								 'b' to 1,
								 'c' to 2,
								 '#' to 3)

		cfgTableMapping[3] = mapOf('S' to listOf("AB", "AB", "AB", eil),
								   'A' to listOf("Da", "Da", ε, ε),
								   'B' to listOf(eil, eil, "cC", eil),
								   'C' to listOf("aADC", eil, eil, ε),
								   'D' to listOf(ε, "b", ε, ε)
								  ) */
	}

	fun <K, V> HashMap<K, V>.of(key: K): V {
		return this[key]!!
	}

}