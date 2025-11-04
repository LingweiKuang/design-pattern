package com.klw.behavioral.interpreter.ast;

import lombok.Getter;

@Getter
public class FieldExpr implements Expr {
    final String name;

    public FieldExpr(String name) {
        this.name = name;
    }
}
