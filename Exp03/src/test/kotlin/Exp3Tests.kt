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
		val testCases = arrayOf("a", "(a)", "(a,a)", "(a,a,a)")

		testCases.forEach {
			assertTrue(useCfg(1, it))
		}

		assertFalse(useCfg(1, "(a,a,)"))
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
		val err = null
		/**
		 * ---grammar---
		 *
		 * ---first---
		 *
		 * ---follow---
		 *
		 * ---index---
		 * +	-	*	/	(	)	$
		 * 0 	1	2	3	4	5	6
		 */
		cfgTableIndex[1] = mapOf()
		cfgTableMapping[1] = mapOf()

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
		 * S:	',', #
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

		cfgTableMapping[2] = mapOf('S' to listOf("(L)", err, "a", err, err),
								   'L' to listOf("ST", err, "ST", err, err),
								   'T' to listOf(err, ε, err, ",ST", err))
		/**
		 *
		 * cfg3不是ll1文法
		 * 原因：对于，A -> Da | ε，需要满足 First(Da) 与 Follow(A) 不相交。
		 * First(Da) = {b,a}
		 * Follow(A) = {b,a,c,#}
		 * 寄！！！！！！！！！！！！！
		 *
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
		 * A: c,b,a,#
		 * B: #
		 * C: c,b,a,#
		 * D: a
		 *
		 * ---index---
		 * 	a	b	c	#
		 * 	0	1	2	3
		 */
		/*cfgTableIndex[3] = mapOf('a' to 0,
									 'b' to 1,
									 'c' to 2,
									 '#' to 3)

			cfgTableMapping[3] = mapOf('S' to listOf("AB", "AB", "AB", err),
									   'A' to listOf("Da", "Da", ε, ε),
									   'B' to listOf(err, err, "cC", err),
									   'C' to listOf("aADC", ε, ε, ε),
									   'D' to listOf(ε, "b", ε, ε)
									  ) */
	}

	fun <K, V> HashMap<K, V>.of(key: K): V {
		return this[key]!!
	}

}