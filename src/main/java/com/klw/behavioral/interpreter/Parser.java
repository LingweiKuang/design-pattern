package com.klw.behavioral.interpreter;

import com.klw.behavioral.interpreter.ast.*;
import com.klw.behavioral.interpreter.tokenizer.Token;
import com.klw.behavioral.interpreter.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int idx = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(idx);
    }

    private Token next() {
        return tokens.get(idx++);
    }

    private boolean accept(TokenType t) {
        if (peek().getType() == t) {
            next();
            return true;
        }
        return false;
    }

    private void expect(TokenType t) {
        if (!accept(t)) throw new RuntimeException("Expected " + t + " but got " + peek());
    }

    // Grammar (simplified):
    // expr := orExpr
    // orExpr := andExpr ( OR andExpr )*
    // andExpr := notExpr ( AND notExpr )*
    // notExpr := NOT notExpr | primary
    // primary := comparison | funcCall | LPAREN expr RPAREN
    // comparison := operand (==|!=|>|<|>=|<=) operand
    // operand := IDENT | STRING | NUMBER
    // funcCall := IDENT LPAREN argList RPAREN

    Expr parseExpression() {
        Expr e = parseOr();
        if (peek().getType() != TokenType.EOF)
            throw new RuntimeException("Unexpected token after expression: " + peek());
        return e;
    }

    private Expr parseOr() {
        Expr left = parseAnd();
        while (accept(TokenType.OR)) {
            Expr right = parseAnd();
            left = new BinaryExpr("OR", left, right);
        }
        return left;
    }

    private Expr parseAnd() {
        Expr left = parseNot();
        while (accept(TokenType.AND)) {
            Expr right = parseNot();
            left = new BinaryExpr("AND", left, right);
        }
        return left;
    }

    private Expr parseNot() {
        if (accept(TokenType.NOT)) return new UnaryExpr("NOT", parseNot());
        return parsePrimary();
    }

    private Expr parsePrimary() {
        Token t = peek();
        if (accept(TokenType.LPAREN)) {
            Expr e = parseOr();
            expect(TokenType.RPAREN);
            return e;
        }
        if (t.getType() == TokenType.IDENT) {
            Token id = next();
            if (accept(TokenType.LPAREN)) {
                // function call
                List<Expr> args = new ArrayList<>();
                if (!accept(TokenType.RPAREN)) {
                    do {
                        args.add(parseOr());
                    } while (accept(TokenType.COMMA));
                    expect(TokenType.RPAREN);
                }
                return new FuncExpr(id.getText(), args);
            } else {
                // Could be start of comparison: IDENT op operand
                Expr left = new FieldExpr(id.getText());
                return parsePossibleComparison(left);
            }
        }
        if (t.getType() == TokenType.STRING) {
            Token s = next();
            Expr lit = new LiteralExpr(s.getText());
            return parsePossibleComparison(lit);
        }
        if (t.getType() == TokenType.NUMBER) {
            Token n = next();
            Expr lit = new LiteralExpr(Double.parseDouble(n.getText()));
            return parsePossibleComparison(lit);
        }
        throw new RuntimeException("Unexpected token in primary: " + t);
    }

    private Expr parsePossibleComparison(Expr left) {
        Token t = peek();
        if (t.getType() == TokenType.EQ || t.getType() == TokenType.NEQ || t.getType() == TokenType.GT ||
                t.getType() == TokenType.GTE || t.getType() == TokenType.LT || t.getType() == TokenType.LTE) {
            String op = next().getText();
            // right operand
            Token rt = peek();
            if (rt.getType() == TokenType.STRING) {
                Expr right = new LiteralExpr(next().getText());
                return new CompareExpr(left, op, right);
            } else if (rt.getType() == TokenType.NUMBER) {
                Expr right = new LiteralExpr(Double.parseDouble(next().getText()));
                return new CompareExpr(left, op, right);
            } else if (rt.getType() == TokenType.IDENT) {
                Expr right = new FieldExpr(next().getText());
                return new CompareExpr(left, op, right);
            } else {
                throw new RuntimeException("Invalid right-hand side of comparison: " + rt);
            }
        }
        return left;
    }
}
