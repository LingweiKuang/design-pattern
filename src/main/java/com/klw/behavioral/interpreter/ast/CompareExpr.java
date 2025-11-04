package com.klw.behavioral.interpreter.ast;

import lombok.Getter;

@Getter
public class CompareExpr implements Expr {
    final Expr left; // usually Field
    final String op; // ==, !=, >, >=, <, <=
    final Expr right; // Literal or Field

    public CompareExpr(Expr left, String op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}
