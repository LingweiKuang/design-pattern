package com.klw.behavioral.interpreter.tokenizer;

public enum TokenType {
    IDENT, NUMBER, STRING,
    EQ, NEQ, GT, LT, GTE, LTE,
    AND, OR, NOT,
    LPAREN, RPAREN, COMMA,
    EOF
}
