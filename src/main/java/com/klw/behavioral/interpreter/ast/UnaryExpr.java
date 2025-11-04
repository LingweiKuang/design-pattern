package com.klw.behavioral.interpreter.ast;

import lombok.Getter;

@Getter
public class UnaryExpr implements Expr {
    final String op; // "NOT"
    final Expr expr;

    public UnaryExpr(String op, Expr expr) {
        this.op = op;
        this.expr = expr;
    }
}
