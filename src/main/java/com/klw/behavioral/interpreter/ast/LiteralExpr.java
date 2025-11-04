package com.klw.behavioral.interpreter.ast;

import lombok.Getter;

@Getter
public class LiteralExpr implements Expr {
    final Object value; // String or Number (Double)

    public LiteralExpr(Object value) {
        this.value = value;
    }
}
