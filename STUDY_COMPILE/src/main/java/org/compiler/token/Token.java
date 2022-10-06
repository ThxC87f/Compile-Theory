package org.compiler.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

    TokenType type;
    String value;

    public static Token newParen(char c) {
        return new Token(TokenType.Paren, Character.toString(c));
    }

}
