package org.compiler.api;

import org.compiler.node.RootNode;
import org.compiler.token.Token;

import java.util.List;

public interface Parser {
    RootNode parseAST(List<Token> tokens);
}