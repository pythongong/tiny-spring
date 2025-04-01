package com.pythongong.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pythongong.stereotype.Nullable;

@FunctionalInterface
public interface Mapper<T> {

    @Nullable
    T map(ResultSet rs, int rowNum) throws SQLException;

}
