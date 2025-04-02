package com.pythongong.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pythongong.stereotype.Nullable;

@FunctionalInterface
public interface RowMapper<T> {

    @Nullable
    T mapRow(ResultSet resultSet, int rowNum) throws SQLException;

}
