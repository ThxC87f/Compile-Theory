package org.compiler.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringTemplate {

    private static final Pattern P = Pattern.compile("\\{}");

    public static String template(String raw, Object... args) {
        if (args.length == 0) {
            return raw;
        }

        AtomicInteger counter = new AtomicInteger();
        Matcher matcher = P.matcher(raw);
        raw = matcher.replaceAll(matchResult -> String.valueOf(args[counter.getAndIncrement()]));

        return raw;
    }

    public static void main(String[] args) {
        System.out.println(template("{}, {}, hello!!!", 1, "Hi", 3));
    }
}
