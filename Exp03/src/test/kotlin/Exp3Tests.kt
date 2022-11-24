import org.compiler.exp01.common.readText
import org.compiler.exp03.api.Grammar
import org.compiler.exp03.api.LL1Table
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
		val cases = arrayOf("a", "(a)", "(a,a)", "(a,a,a)")

		cases.forEach {
			assertTrue(useCfg(2, it))
			println()
		}

		assertFalse(useCfg(2, "(a,a,)"))

	}

	@Test
	fun test_cfg_all() {
		for (i in 1 .. 3) {
			useCfg(i)
		}
	}

	private fun useCfg(i: Int, text: String = ""): Boolean {
		val grammar = Grammar(readText("cfg$i"))
		grammar.ll1TableIndex = cfgTableIndex.of(i)
		grammar.ll1Table = cfgTableMapping.of(i)

		return parser.isLL1Grammar(text, grammar)
	}

	fun <K, V> HashMap<K, V>.of(key: K): V {
		return this[key]!!
	}

	init {
		/**
		 *
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
		 * S:	), ',', #
		 * L:	), ',', #
		 * T:	), ',', #
		 *
		 * ---index---
		 * (	)	a	,
		 * 0 	1	2	3
		 */
		cfgTableIndex[2] = mapOf('(' to 0,
								 ')' to 1,
								 'a' to 2,
								 ',' to 3)
		cfgTableMapping[2] = mapOf('S' to listOf("(L)", null, "a", null),
								   'L' to listOf("ST", null, "ST", null),
								   'T' to listOf(null, "ε", null, ",ST"))

		/**
		 *
		 */
		cfgTableIndex[3] = mapOf()
		cfgTableMapping[3] = mapOf()
	}

}