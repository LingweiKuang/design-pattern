package com.klw.behavioral.interpreter;

import com.klw.behavioral.interpreter.ast.Expr;
import com.klw.behavioral.interpreter.sql.SqlGenerator;
import com.klw.behavioral.interpreter.sql.SqlResult;
import com.klw.behavioral.interpreter.tokenizer.Token;
import com.klw.behavioral.interpreter.tokenizer.Tokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class InterpreterDemo {
    // TODO
    public static void main(String[] args) {
        String expr = "country == \"JP\" AND (revenue > 10000 OR user_age >= 30) AND NOT test_user";
        runDemo(expr);

        // Example evaluate against a row
        Map<String, Object> row = new HashMap<>();
        row.put("country", "JP");
        row.put("revenue", 15000.0);
        row.put("user_age", 25);
        row.put("test_user", "");
        Predicate<Map<String, Object>> pred = PredicateInterpreter.compileToPredicate(parse(expr));
        System.out.println("Row matches expr? " + pred.test(row));
    }

    static void runDemo(String expr) {
        System.out.println("Parsing: " + expr);
        Expr ast = parse(expr);
        System.out.println("AST built: " + ast.getClass().getSimpleName());
        SqlResult sql = SqlGenerator.toSql(ast);
        System.out.println("SQL WHERE: " + sql.getWhere() + " params=" + sql.getParams());
        System.out.println("----");
    }

    static Expr parse(String s) {
        Tokenizer t = new Tokenizer(s);
        List<Token> toks = t.tokenize();
        Parser p = new Parser(toks);
        return p.parseExpression();
    }
}
