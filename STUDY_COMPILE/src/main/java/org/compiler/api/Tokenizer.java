package org.compiler.api;

import org.compiler.token.Token;

import java.util.List;

public interface Tokenizer {

    List<Token> parseToken(String code);


}
