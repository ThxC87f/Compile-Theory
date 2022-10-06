package org.compiler;

import org.compiler.api.Parser;
import org.compiler.api.Tokenizer;
import org.compiler.api.Traverser;
import org.compiler.api.impl.ParserImpl;
import org.compiler.api.impl.TokenizerImpl;
import org.compiler.api.impl.TraverserImpl;
import org.compiler.node.CallExpressionNode;
import org.compiler.node.LiteralNode;
import org.compiler.node.RootNode;
import org.compiler.token.Token;
import org.compiler.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCompiler {

    static final Tokenizer tokenizer = new TokenizerImpl();
    static final Parser parser = new ParserImpl();

    static final Traverser traverser = new TraverserImpl();

    static final String code = "(add 2 (subtract 4 2))";


    @Test
    public void t00() {
        System.out.println(Character.isLetter('a'));
        System.out.println(Character.isLetter('中'));
    }

    @Test(timeout = 100)
    public void t01() {
        List<Token> tokens = tokenizer.parseToken(code);
        Assert.assertArrayEquals(tokens.toArray(), new Token[]{
                new Token(TokenType.Paren, "("),
                new Token(TokenType.Name, "add"),
                new Token(TokenType.Number, "2"),
                new Token(TokenType.Paren, "("),
                new Token(TokenType.Name, "subtract"),
                new Token(TokenType.Number, "4"),
                new Token(TokenType.Number, "2"),
                new Token(TokenType.Paren, ")"),
                new Token(TokenType.Paren, ")"),
        });
    }

    @Test()
    public void t03() {
        List<Token> list = new ArrayList<>();
        list.add(new Token(TokenType.Paren, "("));
        list.add(new Token(TokenType.Name, "add"));
        list.add(new Token(TokenType.Number, "2"));
        list.add(new Token(TokenType.Paren, ")"));
        RootNode root = parser.parseAST(list);
        System.out.println(root);
    }

    @Test(timeout = 100)
    public void test_compile() {
        RootNode expected = new RootNode(
                List.of(new CallExpressionNode("add",
                        (List.of(
                                new LiteralNode<>("2"),
                                new CallExpressionNode("subtract", (
                                        List.of(new LiteralNode<>("4"),
                                                new LiteralNode<>("2")
                                        ))
                                )
                        )
                        )
                ))
        );
        Assert.assertEquals(expected, parser.parseAST(tokenizer.parseToken(code)));
    }

    @Test
    public void test_exception() {
        List<Token> list = new ArrayList<>();
        list.add(new Token(TokenType.Paren, "("));
        list.add(new Token(TokenType.Name, "add"));
        list.add(new Token(TokenType.Number, "2"));
        list.add(new Token(null, "("));
        try {
            parser.parseAST(list);
        } catch (Exception e) {
            System.out.println("caught!");
            Assert.assertEquals("IllegalArgumentException", e.getClass().getSimpleName());
        }
    }


    @Test
    public void test_traverse() {
        List<String> orders = new ArrayList<>();

        traverser.traverse(getAST(), node -> {
            if (node instanceof LiteralNode) {
                orders.add(((LiteralNode<?>) node).getValue().toString());
            } else if (node instanceof CallExpressionNode) {
                orders.add(((CallExpressionNode) node).getName());
            } else {
                orders.add(node.getType().toString());
            }

        });
        orders.forEach(System.out::println);
    }

    private RootNode getAST() {
        List<Token> tokens = tokenizer.parseToken(code);
        return parser.parseAST(tokens);
    }
}
