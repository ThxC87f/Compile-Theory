package org.compiler.exp04

import org.compiler.exp01.api.Token
import org.compiler.exp01.common.CompileException
import org.compiler.exp01.common.TokenRegister

/**
 * @author Straw
 */
class SemanticTranslator {

	companion object {
		val TOKEN_ADD = Token("+")
		val TOKEN_SUB = Token("-")
		val TOKEN_MUL = Token("*")
		val TOKEN_DIV = Token("/")
		val TOKEN_L_PAR = Token("(")
		val TOKEN_R_PAR = Token(")")
		val TOKEN_DIGIT = Token("/", TokenRegister.DIGIT_TYPE_CODE)
	}

	fun translateStatement(tokens: List<Token>): Number {
		return SemanticTranslationProcessor(tokens).getValue()
	}

	@Suppress(
		"FunctionName",
		"NOTHING_TO_/**/",
		"MemberVisibilityCanBePrivate",
		"LocalVariableName",
		"UnnecessaryVariable",
		"UNUSED_VARIABLE"
	)
	class SemanticTranslationProcessor(val tokens: List<Token>) {
		var cur: Int = 0

		/**
		 * E->T  {R.i=T.val}
		 * R  {E.val=R.s}
		 */
		fun E(): Double {
			val T_val: Double = T()
			val (R_i, R_s) = R(R_i = T_val)
			val E_val: Double = R_s
			return E_val
		}

		/**
		 * R->+
		 * T    {R1.i=R.i+T.val}
		 * R1    {R.s = R1.s }
		 *  |
		 * -
		 * T     {R1.i=R.i-T.val}
		 * R1     {R.s=R1.s}
		 *  |
		 * ε    {R.s=R.i}
		 *
		 */
		fun R(R_i: Double): Pair<Double, Double> {
			if (isEmpty()) {
				return Pair(R_i, R_i)
			}

			when (peekToken()) {
				TOKEN_ADD -> {
					next()
					val T_val = T()
					val (R1_i, R1_s) = R(R_i + T_val)
					val R_s = R1_s
					return Pair(R_i, R_s)
				}

				TOKEN_SUB -> {
					next()
					val T_val = T()
					val (R1_i, R1_s) = R(R_i - T_val)
					val R_s = R1_s
					return Pair(R_i, R_s)
				}

				else -> {
					return Pair(R_i, R_i)
				}
			}
		}

		/**
		 * T->F     {Q.i=F.val}
		 * Q    {T.val=Q.s}
		 *
		 */
		fun T(): Double {
			val F_val: Double = F()
			val (Q_i, Q_s) = Q(Q_i = F_val)
			val T_val = Q_s
			return T_val
		}

		/**
		 * Q->*
		 * F      {Q1.i=Q.i*F.val}
		 * Q1    {Q.s=Q1.s}
		 *  |
		 *  /
		 * F       {Q1.i=Q.i/F.val}
		 * Q1     {Q.s=Q1.s}
		 *  |
		 * ε       {Q.s=Q.i}
		 *
		 */
		fun Q(Q_i: Double): Pair<Double, Double> {
			if (isEmpty()) {
				return Pair(Q_i, Q_i)
			}

			when (peekToken()) {
				TOKEN_MUL -> {
					next()
					val F_val = F()
					val (Q1_i, Q1_s) = Q(Q_i * F_val)
					val Q_s = Q1_s
					return Pair(Q_i, Q_s)
				}

				TOKEN_DIV -> {
					next()
					val F_val = F()
					val (Q1_i, Q1_s) = Q(Q_i / F_val)
					val Q_s = Q1_s
					return Pair(Q_i, Q_s)
				}

				else -> {
					return Pair(Q_i, Q_i)
				}
			}
		}

		/**
		 * F-> (E)      { F.val=E.val}
		 * |
		 *  Num      {F.val = num.lex_val}
		 */
		fun F(): Double {
			val F_val: Double
			when (val it = nextToken()) {
				TOKEN_L_PAR -> {
					val E_val = E()
					if (nextToken() != TOKEN_R_PAR) {
						throw CompileException("分析失败，缺失 ')'")
					}
					F_val = E_val
				}

				TOKEN_DIGIT -> {
					F_val = it.value.toDouble()
				}

				else -> {
					throw CompileException("""
						|F-> (E)      { F.val=E.val}
						||
						| Num      {F.val = num.lex_val}
					""".trimMargin())
				}
			}

			return F_val
		}

		/**/ fun peekToken(): Token {
			return tokens[cur]
		}

		/**/ fun nextToken(): Token {
			return tokens[cur++]
		}

		/**/ fun isEmpty(): Boolean {
			return cur == tokens.size
		}

		/**/ fun next() {
			cur++
		}

		fun getValue(): Double {
			return E()
		}

	}


}