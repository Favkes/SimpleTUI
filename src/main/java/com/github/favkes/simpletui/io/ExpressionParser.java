package com.github.favkes.simpletui.io;

import com.github.favkes.simpletui.Logger;

import java.util.function.IntUnaryOperator;

interface IntExpression {
    int eval(int row);
    default IntUnaryOperator compile() {
        return this::eval;
    }
}

public class ExpressionParser {
    public static final Logger log = Logger.logger();

    private final String s;
    private int pos = 0;

    private ExpressionParser(String s) {
        this.s = s.replaceAll("\\s+", "");
    }

    public static IntExpression parse(String s) {
        return new ExpressionParser(s).parseExpr();
    }

    private IntExpression parseExpr() {
        IntExpression left = parseTerm();

        while (pos < s.length()) {
            char op = s.charAt(pos);
            if (op != '+' && op != '-') break;
            pos++;

            IntExpression right = parseTerm();
            left = binary(left, right, op);
        }
        return left;
    }

    private IntExpression parseTerm() {
        IntExpression left = parseFactor();

        while (pos < s.length()) {
            char op = s.charAt(pos);
            if (op != '*' && op != '/') break;
            pos++;

            IntExpression right = parseFactor();
            left = binary(left, right, op);
        }
        return left;
    }

    private IntExpression parseFactor() {
        if (s.charAt(pos) == '(') {
            pos++;
            IntExpression e = parseExpr();
            pos++;
            return e;
        }
        if (s.startsWith("row", pos)) {
            pos += 3;
            return row -> row;
        }

        int start = pos;
        while (pos < s.length() && Character.isDigit(s.charAt(pos))) {
            pos++;
        }
        int value = Integer.parseInt(s.substring(start, pos));
        return row -> value;
    }

    private IntExpression binary(
            IntExpression a,
            IntExpression b,
            char op
    ) {
        return switch (op) {
            case '+' -> row -> a.eval(row) + b.eval(row);
            case '-' -> row -> a.eval(row) - b.eval(row);
            case '*' -> row -> a.eval(row) * b.eval(row);
            case '/' -> row -> a.eval(row) / b.eval(row);
            default -> throw new IllegalStateException();
        };
    }
}