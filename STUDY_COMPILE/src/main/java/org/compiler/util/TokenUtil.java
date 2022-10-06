package org.compiler.util;

import org.compiler.token.Token;
import org.compiler.token.TokenType;

public class TokenUtil {
    public static boolean isLeftPar(Token token) {
        return token.getType() == TokenType.Paren && token.getValue().equals("(");
    }

    public static boolean isRightPar(Token token) {
        return token.getType() == TokenType.Paren && token.getValue().equals(")");
    }

    public static boolean isNumber(Token token) {
        return token.getType() == TokenType.Number;
    }

    public static boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') ||
                ('A' <= c && c <= 'Z');
    }
}
