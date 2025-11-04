package com.klw.behavioral.interpreter.tokenizer;

import lombok.Getter;

@Getter
public class Token {
    final TokenType type;
    final String text;

    Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String toString() {
        return type + ":" + text;
    }
}
