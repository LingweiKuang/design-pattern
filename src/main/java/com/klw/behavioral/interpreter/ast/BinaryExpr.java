package com.klw.behavioral.interpreter.ast;

import lombok.Getter;

@Getter
public class BinaryExpr implements Expr {
    final String op; // "AND" / "OR"
    final Expr left, right;

    public BinaryExpr(String op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
