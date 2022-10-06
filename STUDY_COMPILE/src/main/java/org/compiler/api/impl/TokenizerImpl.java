package org.compiler.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.compiler.api.Tokenizer;
import org.compiler.token.Token;
import org.compiler.token.TokenType;
import org.compiler.util.TokenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
public class TokenizerImpl implements Tokenizer {

    private static final Map<Predicate<Character>, TokenType> TOKEN_MAPPING = new HashMap<>();

    static {
        TOKEN_MAPPING.put(TokenUtil::isLetter, TokenType.Name);
        TOKEN_MAPPING.put(Character::isDigit, TokenType.Number);
    }

    public List<Token> parseToken(String code) {
        List<Token> tokens = new ArrayList<>();

        char pointer;
        int index = 0;

        while (index < code.length()) {
            pointer = code.charAt(index);

            if (pointer == ' ') {
                index++;
                continue;
            }

            if (pointer == '(' || pointer == ')') {
                tokens.add(Token.newParen(pointer));
                index++;
                continue;
            }

            // 下面解析 英文字符 或 数字
            boolean isContinue = false;
            for (Map.Entry<Predicate<Character>, TokenType> entry : TOKEN_MAPPING.entrySet()) {
                Object[] tuple = predicateToken(entry.getKey(), entry.getValue(), code, index);
                // 0 -> token
                // 1 -> new index
                Token token = (Token) tuple[0];
                if (token != null) {
                    // 符合当前token，可以退出了
                    tokens.add(token);
                    index = (int) tuple[1];
                    isContinue = true;
                    break;
                }
            }
            if (isContinue)
                continue;

            log.error("i can't understand '{}' of \"{}\"", pointer, code);
        }

        return tokens;
    }


    private Object[] predicateToken(Predicate<Character> predicate, TokenType type, String code, int index) {
        Object[] ret = new Object[]{null, null};

        char chr = code.charAt(index);
        if (predicate.test(chr)) {
            StringBuilder builder = new StringBuilder();
            while (predicate.test(chr)) {
                builder.append(chr);
                index++;
                chr = code.charAt(index);
            }
            ret[0] = new Token(type, builder.toString());
        }
        ret[1] = index;

        return ret;
    }


}
