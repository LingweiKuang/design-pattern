package com.klw.behavioral.interpreter;

import com.klw.behavioral.interpreter.ast.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

// ---------- Interpreter: AST -> Predicate<Map<String,Object>> ----------
public class PredicateInterpreter {
    // For simplicity, fields come from Map<String, Object>
    public static Predicate<Map<String, Object>> compileToPredicate(Expr ast) {
        // TODO Predicate:
        // map -> "VIP".equals(map.get("type"))
        // predicate1.and(predicate2).test(map)
        // predicate1.or(predicate2).test(map)
        // predicate1.negate().test(map)
        // Predicate.isEqual(expectedMap)
        // List<Map<String, Object>> vipUsers = List<Map<String, Object>>.stream()
        //        .filter(Predicate)
        //        .collect(Collectors.toList());
        return map -> eval(ast, map);
    }

    // 解释评估: Map<String, Object>
    private static boolean eval(Expr e, Map<String, Object> row) {
        if (e instanceof BinaryExpr) {
            BinaryExpr b = (BinaryExpr) e;
            if ("AND".equals(b.getOp())) return eval(b.getLeft(), row) && eval(b.getRight(), row);
            else return eval(b.getLeft(), row) || eval(b.getRight(), row);
        }
        if (e instanceof UnaryExpr) {
            UnaryExpr u = (UnaryExpr) e;
            if ("NOT".equals(u.getOp())) return !eval(u.getExpr(), row);
        }
        if (e instanceof CompareExpr) {
            CompareExpr c = (CompareExpr) e;
            Object lv = resolveValue(c.getLeft(), row);
            Object rv = resolveValue(c.getRight(), row);
            return compareValues(lv, rv, c.getOp());
        }
        if (e instanceof FuncExpr) {
            FuncExpr f = (FuncExpr) e;
            if (f.getName().equalsIgnoreCase("contains")) {
                if (f.getArgs().size() != 2) throw new RuntimeException("contains expects 2 args");
                Object left = resolveValue(f.getArgs().get(0), row);
                Object right = resolveValue(f.getArgs().get(1), row);
                return left != null && right != null && left.toString().contains(right.toString());
            } else if (f.getName().equalsIgnoreCase("startsWith")) {
                Object left = resolveValue(f.getArgs().get(0), row);
                Object right = resolveValue(f.getArgs().get(1), row);
                return left != null && right != null && left.toString().startsWith(right.toString());
            } else {
                throw new RuntimeException("Unknown function: " + f.getName());
            }
        }
        if (e instanceof LiteralExpr) {
            Object v = ((LiteralExpr) e).getValue();
            return truthy(v);
        }
        if (e instanceof FieldExpr) {
            Object v = row.get(((FieldExpr) e).getName());
            return truthy(v);
        }
        throw new RuntimeException("Unknown expr: " + e);
    }

    private static Object resolveValue(Expr e, Map<String, Object> row) {
        if (e instanceof LiteralExpr) return ((LiteralExpr) e).getValue();
        if (e instanceof FieldExpr) return row.get(((FieldExpr) e).getName());
        // For nested expressions (rare) evaluate boolean truthiness
        if (e instanceof FuncExpr) {
            return eval(e, row);
        }
        throw new RuntimeException("Unsupported value expression: " + e.getClass());
    }

    private static boolean compareValues(Object lv, Object rv, String op) {
        if (lv == null || rv == null) {
            if (op.equals("==")) return Objects.equals(lv, rv);
            if (op.equals("!=")) return !Objects.equals(lv, rv);
            return false; // other comparisons false if null
        }
        // number comparison?
        if (lv instanceof Number || rv instanceof Number) {
            double a = toDouble(lv);
            double b = toDouble(rv);
            switch (op) {
                case "==":
                    return a == b;
                case "!=":
                    return a != b;
                case ">":
                    return a > b;
                case "<":
                    return a < b;
                case ">=":
                    return a >= b;
                case "<=":
                    return a <= b;
            }
        }
        // string comparison
        String sa = lv.toString();
        String sb = rv.toString();
        switch (op) {
            case "==":
                return sa.equals(sb);
            case "!=":
                return !sa.equals(sb);
            case ">":
                return sa.compareTo(sb) > 0;
            case "<":
                return sa.compareTo(sb) < 0;
            case ">=":
                return sa.compareTo(sb) >= 0;
            case "<=":
                return sa.compareTo(sb) <= 0;
        }
        return false;
    }

    private static double toDouble(Object o) {
        return ((Number) o).doubleValue();
    }

    private static boolean truthy(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof Number) return ((Number) v).doubleValue() != 0;
        return !v.toString().isEmpty();
    }
}
