package org.compiler.api.impl;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.compiler.api.Parser;
import org.compiler.node.CallExpressionNode;
import org.compiler.node.LiteralNode;
import org.compiler.node.Node;
import org.compiler.node.RootNode;
import org.compiler.token.Token;

import java.util.List;

import static org.compiler.util.TokenUtil.*;

public class ParserImpl implements Parser {

    public Node walkOneNode(TokenMetaDataHolder meta) {
        Token tk = meta.getAndNext();
        // 1
        if (isNumber(tk)) {
            return new LiteralNode<>(tk.getValue());
        }

        // "(add 2 (subtract 4 2))"
        if (isLeftPar(tk)) {
            tk = meta.getAndNext();
            CallExpressionNode func = new CallExpressionNode(tk.getValue());
            while (!isRightPar(meta.curToken())) {
                // add -> 2 -> (subtract 4 2)
                func.addBodyNode(walkOneNode(meta));
            }

            // 走到这里，表明 meta.curToken() 是 ')'
            meta.next();
            return func;
        }

        throw new IllegalArgumentException(String.format("当前token=%s, meta=%s", meta.offsetToken(-1), meta));
    }

    @Override
    public RootNode parseAST(List<Token> tokens) {
        RootNode root = new RootNode();
        TokenMetaDataHolder meta = new TokenMetaDataHolder(0, tokens);
        while (meta.current < tokens.size()) {
            root.addBodyNode(walkOneNode(meta));
        }

        return root;
    }

    @AllArgsConstructor
    @ToString
    static class TokenMetaDataHolder {
        int current;
        List<Token> tokens;

        public Token curToken() {
            return offsetToken(0);
        }

        public Token getAndNext() {
            Token tk = curToken();
            next();
            return tk;
        }

        public Token offsetToken(int offset) {
            current += offset;
            return tokens.get(current);
        }

        @SuppressWarnings("UnusedReturnValue")
        public TokenMetaDataHolder next() {
            current++;
            return this;
        }

    }
}
