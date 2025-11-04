package com.klw.behavioral.interpreter.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Pattern TOKEN_PAT = Pattern.compile(
            "\\s*(?:(and|AND|or|OR|not|NOT)|([A-Za-z_][A-Za-z0-9_]*)|(>=|<=|==|!=|>|<)|(\"(?:\\\\.|[^\"])*\")|(\\d+(?:\\.\\d+)?)|(\\()|(\\))|(,))",
            Pattern.CASE_INSENSITIVE);
    private final String input;
    private final Matcher m;
    private int pos = 0;

    public Tokenizer(String input) {
        this.input = input;
        this.m = TOKEN_PAT.matcher(input);
    }

    public List<Token> tokenize() {
        List<Token> out = new ArrayList<>();
        while (pos < input.length()) {
            if (!m.find(pos) || m.start() != pos) {
                throw new RuntimeException("Unexpected token at pos " + pos + ": '" + input.substring(pos, Math.min(pos + 20, input.length())) + "'");
            }
            pos = m.end();
            if (m.group(1) != null) { // logical words
                String w = m.group(1).toUpperCase();
                if (w.equals("AND")) out.add(new Token(TokenType.AND, w));
                else if (w.equals("OR")) out.add(new Token(TokenType.OR, w));
                else if (w.equals("NOT")) out.add(new Token(TokenType.NOT, w));
            } else if (m.group(2) != null) {
                out.add(new Token(TokenType.IDENT, m.group(2)));
            } else if (m.group(3) != null) {
                String op = m.group(3);
                switch (op) {
                    case "==":
                        out.add(new Token(TokenType.EQ, op));
                        break;
                    case "!=":
                        out.add(new Token(TokenType.NEQ, op));
                        break;
                    case ">":
                        out.add(new Token(TokenType.GT, op));
                        break;
                    case "<":
                        out.add(new Token(TokenType.LT, op));
                        break;
                    case ">=":
                        out.add(new Token(TokenType.GTE, op));
                        break;
                    case "<=":
                        out.add(new Token(TokenType.LTE, op));
                        break;
                    default:
                        throw new RuntimeException("Unknown operator " + op);
                }
            } else if (m.group(4) != null) {
                String str = m.group(4);
                // remove surrounding quotes and unescape simplistic
                str = str.substring(1, str.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
                out.add(new Token(TokenType.STRING, str));
            } else if (m.group(5) != null) {
                out.add(new Token(TokenType.NUMBER, m.group(5)));
            } else if (m.group(6) != null) {
                out.add(new Token(TokenType.LPAREN, "("));
            } else if (m.group(7) != null) {
                out.add(new Token(TokenType.RPAREN, ")"));
            } else if (m.group(8) != null) {
                out.add(new Token(TokenType.COMMA, ","));
            }
        }
        out.add(new Token(TokenType.EOF, ""));
        return out;
    }
}
