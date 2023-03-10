package org.compiler.exp02.api.impl

import org.compiler.exp01.api.Token
import org.compiler.exp01.common.CompileException
import org.compiler.exp01.common.TokenRegister
import org.compiler.exp02.api.RecursiveDescentParser

class Exp2Parser : RecursiveDescentParser {
	
	companion object {
		val TOKEN_ADD = Token("+")
		val TOKEN_SUB = Token("-")
		val TOKEN_MUL = Token("*")
		val TOKEN_DIV = Token("/")
		val TOKEN_L_PAR = Token("(")
		val TOKEN_R_PAR = Token(")")
		val TOKEN_ID = Token("id", TokenRegister.IDENTIFIER_TYPE_CODE)
		val TOKEN_DIGIT = Token("/", TokenRegister.DIGIT_TYPE_CODE)
	}
	
	override fun judgeText(tokens: List<Token>): Boolean {
		return RecursiveDescentProcessor(tokens).makeSense()
	}
	
	@Suppress("FunctionName", "NOTHING_TO_INLINE")
	class RecursiveDescentProcessor(val tokens: List<Token>) {
		var cur: Int = 0
		
		fun E(lang: String = "E -> TE'") {
			tipLang(lang)
			T()
			E_()
		}
		
		fun E_(lang: Array<String> = arrayOf("E'-> ", "+TE'", "-TE'", "ε")) {
			if (isEmpty()) {
				tipLang(lang, 3)
				return
			}
			
			when (peekToken()) {
				TOKEN_ADD -> {
					tipLang(lang, 1)
					next()
					T()
					E_()
				}
				
				TOKEN_SUB -> {
					tipLang(lang, 2)
					next()
					T()
					E_()
				}
				
				else -> {
					tipLang(lang, 3)
				}
			}
		}
		
		fun T(lang: String = "T -> FT'") {
			tipLang(lang)
			F()
			T_()
		}
		
		fun T_(lang: Array<String> = arrayOf("T'-> ", "*FT',", "/FT'", "ε")) {
			if (isEmpty()) {
				tipLang(lang, 3)
				return
			}
			
			when (peekToken()) {
				TOKEN_MUL -> {
					tipLang(lang, 1)
					next()
					F()
					T_()
				}
				
				TOKEN_DIV -> {
					tipLang(lang, 2)
					next()
					F()
					T_()
				}
				
				else -> {
					tipLang(lang, 3)
				}
			}
		}
		
		fun F(lang: Array<String> = arrayOf("F -> ", "(E)", "id", "num")) {
			when (val it = nextToken()) {
				TOKEN_L_PAR -> {
					tipLang(lang, 1)
					E()
					if (nextToken() != TOKEN_R_PAR) {
						throw CompileException("分析失败，不满足文法: [$lang]")
					}
					return
				}
				
				TOKEN_ID -> {
					tipLang(lang, 2)
					return
				}
				
				TOKEN_DIGIT -> {
					tipLang(lang, 3)
					return
				}
				
				else -> {
					UnsatisfiedTokenException(it, lang)
				}
			}
		}
		
		private fun UnsatisfiedTokenException(it: Token, lang: Array<String>) {
			val tip = lang.filterIndexed { i, _ -> i > 0 }.toString()
			throw CompileException("分析失败，当前[${it}]不满足 ${lang[0]}${tip}之中的任意一个")
		}
		
		fun makeSense(): Boolean {
			var isMakeSense = true
			try {
				E()
			} catch (e: CompileException) {
				println(e.message)
				isMakeSense = false
			}
			
			return isMakeSense
		}
		
		inline fun peekToken(): Token {
			return tokens[cur]
		}
		
		inline fun nextToken(): Token {
			return tokens[cur++]
		}
		
		inline fun isEmpty(): Boolean {
			return cur == tokens.size
		}
		
		inline fun next() {
			cur++
		}
		
		inline fun tipLang(lang: String) {
			println("使用文法：$lang")
		}
		
		inline fun tipLang(lang: Array<String>, index: Int) {
			println("选用候选式：${lang[0]}${lang[index]}")
		}
	}
}