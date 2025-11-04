package com.klw.behavioral.interpreter.ast;

import lombok.Getter;

import java.util.List;

@Getter
public class FuncExpr implements Expr {
    final String name;
    final List<Expr> args;

    public FuncExpr(String name, List<Expr> args) {
        this.name = name;
        this.args = args;
    }
}
