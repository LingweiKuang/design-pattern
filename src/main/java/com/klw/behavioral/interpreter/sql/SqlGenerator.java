package com.klw.behavioral.interpreter.sql;

import com.klw.behavioral.interpreter.ast.*;

import java.util.ArrayList;
import java.util.List;

public class SqlGenerator {
    public static SqlResult toSql(Expr ast) {
        List<Object> params = new ArrayList<>();
        String where = build(ast, params);
        return new SqlResult(where, params);
    }

    private static String build(Expr e, List<Object> params) {
        if (e instanceof BinaryExpr) {
            BinaryExpr b = (BinaryExpr) e;
            String l = build(b.getLeft(), params);
            String r = build(b.getRight(), params);
            return "(" + l + " " + b.getOp() + " " + r + ")";
        }
        if (e instanceof UnaryExpr) {
            UnaryExpr u = (UnaryExpr) e;
            return "(NOT " + build(u.getExpr(), params) + ")";
        }
        if (e instanceof CompareExpr) {
            CompareExpr c = (CompareExpr) e;
            String left = atomToSql(c.getLeft(), params);
            String right = atomToSql(c.getRight(), params);
            String op = c.getOp().equals("==") ? "=" : c.getOp();
            return left + " " + op + " " + right;
        }
        if (e instanceof FuncExpr) {
            FuncExpr f = (FuncExpr) e;
            if (f.getName().equalsIgnoreCase("contains")) {
                // assume args: field, literal
                String col = atomToSql(f.getArgs().get(0), params);
                Object lit = ((LiteralExpr) f.getArgs().get(1)).getValue();
                params.add("%" + lit.toString() + "%");
                return col + " LIKE ?";
            }
            if (f.getName().equalsIgnoreCase("startsWith")) {
                String col = atomToSql(f.getArgs().get(0), params);
                Object lit = ((LiteralExpr) f.getArgs().get(1)).getValue();
                params.add(lit.toString() + "%");
                return col + " LIKE ?";
            }
            throw new RuntimeException("Unsupported function for SQL: " + f.getName());
        }
        if (e instanceof LiteralExpr) {
            params.add(((LiteralExpr) e).getValue());
            return "?";
        }
        if (e instanceof FieldExpr) {
            return quote(((FieldExpr) e).getName());
        }
        throw new RuntimeException("Unsupported node for SQL: " + e.getClass());
    }

    private static String atomToSql(Expr e, List<Object> params) {
        if (e instanceof FieldExpr) return quote(((FieldExpr) e).getName());
        if (e instanceof LiteralExpr) {
            params.add(((LiteralExpr) e).getValue());
            return "?";
        }
        // fallback: build subexpr
        return "(" + build(e, params) + ")";
    }

    private static String quote(String ident) {
        // naive quoting; in production use proper identifier quoting per DB
        if (ident.matches("^[A-Za-z_][A-Za-z0-9_]*$")) return "\"" + ident + "\"";
        throw new RuntimeException("Invalid identifier for SQL: " + ident);
    }
}
