package com.pythongong.jdbc;

import java.util.List;

import com.pythongong.util.CheckUtils;

public record UpdateParam(String sql, List<Object> argus) {

    public UpdateParam {
        CheckUtils.emptyString(sql, "UpdateParam", "sql");

    }

}
