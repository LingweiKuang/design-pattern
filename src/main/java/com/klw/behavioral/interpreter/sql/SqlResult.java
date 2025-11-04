package com.klw.behavioral.interpreter.sql;

import lombok.Getter;

import java.util.List;

@Getter
public class SqlResult {
    final String where;
    final List<Object> params;

    SqlResult(String where, List<Object> params) {
        this.where = where;
        this.params = params;
    }
}
