package com.pythongong.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.pythongong.stereotype.Nullable;

@FunctionalInterface
public interface ConnectionCallback<T> {
    @Nullable
    T doInConnection(Connection connection) throws SQLException;
}
