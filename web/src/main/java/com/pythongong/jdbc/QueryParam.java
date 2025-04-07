package com.pythongong.jdbc;

import java.util.List;

import com.pythongong.util.CheckUtils;

import lombok.Builder;

@Builder
public record QueryParam(String sql, Class<?> reuiredClass, List<Object> argus) {

    public QueryParam {
        CheckUtils.nullArgs(sql, "QueryParam", "sql");
    }

}
