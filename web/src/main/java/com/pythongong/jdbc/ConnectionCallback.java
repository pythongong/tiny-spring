/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pythongong.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.pythongong.stereotype.Nullable;

/**
 * Callback interface for JDBC code that works with a Connection.
 * 
 * <p>Used for executing arbitrary JDBC operations within a managed JDBC Connection.
 * The Connection is provided by the framework, which handles proper connection
 * handling and exception translation.
 *
 * <p>This interface is designed to be used as a lambda expression or method reference
 * for simple JDBC operations.
 *
 * @param <T> the result type of the callback
 * @author Cheng Gong
 * @since 1.0
 */
@FunctionalInterface
public interface ConnectionCallback<T> {
    
    /**
     * Gets called by {@code JdbcTemplate} with an active JDBC Connection.
     * Does not need to care about activating or closing the Connection.
     *
     * @param connection active JDBC Connection
     * @return a result object, or null if none
     * @throws SQLException if a SQLException is encountered
     */
    @Nullable
    T doInConnection(Connection connection) throws SQLException;
}
