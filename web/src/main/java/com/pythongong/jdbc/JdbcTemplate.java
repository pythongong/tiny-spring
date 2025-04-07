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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.pythongong.exception.DataAccessException;
import com.pythongong.jdbc.transaction.DataSourceTransactionManager;
import com.pythongong.util.ClassUtils;

/**
 * Core class for JDBC database operations.
 * 
 * <p>This class simplifies JDBC operations by handling resource creation,
 * statement preparation, and exception translation. It supports both
 * transactional and non-transactional execution of database operations.
 *
 * <p>Main features include:
 * <ul>
 *   <li>Simplified execution of JDBC operations
 *   <li>Exception translation into DataAccessExceptions
 *   <li>Support for both queries and updates
 *   <li>Integration with transaction management
 * </ul>
 *
 * @author Cheng Gong
 * @since 1.0
 */
public class JdbcTemplate {

    /** Default fetch size for queries */
    private static final int DEFAULT_SIZE = 10;

    /** The DataSource to obtain connections from */
    private final DataSource dataSource;

    /**
     * Create a new JdbcTemplate for the given DataSource.
     * 
     * @param dataSource the JDBC DataSource to obtain connections from
     */
    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Execute a JDBC operation with a Connection callback.
     *
     * @param <T> the result type
     * @param action callback object that specifies the JDBC operation
     * @return a result object returned by the action, or null
     * @throws DataAccessException if there is any problem
     */
    public <T> T execute(ConnectionCallback<T> action) {
        Connection transactionalConnection = DataSourceTransactionManager.getConnection();
        if (transactionalConnection != null) {
            try {
                return action.doInConnection(transactionalConnection);
            } catch (SQLException e) {
                throw new DataAccessException("Connect to database failed");
            }
        }

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            T result = action.doInConnection(connection);
            connection.setAutoCommit(false);
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Connect to database failed");
        }
    }

    /**
     * Execute an update statement.
     *
     * @param param the update parameters
     * @return the number of rows affected
     * @throws DataAccessException if there is any problem executing the update
     */
    public int update(UpdateParam param) throws DataAccessException {
        return execute((connection) -> {
            try (PreparedStatement statement = createStatement(connection, param.sql(), param.argus())) {
                return statement.executeUpdate();
            }
        });
    }

    /**
     * Execute an insert statement and return the generated key.
     *
     * @param param the update parameters
     * @return the generated key value
     * @throws DataAccessException if there is any problem or no key was generated
     */
    public Number updateAndGetKey(UpdateParam param) {
        return execute((connection) -> {
            try (PreparedStatement statement = createStatement(connection, param.sql(), param.argus())) {
                int updateNum = statement.executeUpdate();
                if (updateNum == 0) {
                    throw new DataAccessException("No update");
                }

                if (updateNum > 1) {
                    throw new DataAccessException("Multiple update");
                }
                Object number = null;

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    number = generatedKeys.getObject(1);
                }
                if (number == null) {
                    throw new DataAccessException("No key");
                }
                if (!(number instanceof Number)) {
                    throw new DataAccessException("Key type is wrong");
                }
                return (Number) number;
            }

        });
    }

    /**
     * Query for a single object.
     *
     * @param <T> the type of object to return
     * @param param the query parameters
     * @return the single result object, or null if none found
     * @throws DataAccessException if multiple results found or query fails
     */
    public <T> T queryOne(QueryParam param) throws DataAccessException {
        return execute((connection) -> {
            List<T> results = doQuery(connection, param, 1);
            if (ClassUtils.isCollectionEmpty(results)) {
                return null;
            }
            return results.iterator().next();

        });
    }

    /**
     * Query for a list of objects.
     *
     * @param <T> the type of object in the list
     * @param param the query parameters
     * @return the result list, empty if no result
     * @throws DataAccessException if there is any problem
     */
    public <T> List<T> queryList(QueryParam param) throws DataAccessException {
        return execute((connection) -> {
            return doQuery(connection, param, DEFAULT_SIZE);
        });
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> doQuery(Connection connection, QueryParam param, int size) {
        try (PreparedStatement statement = createStatement(connection, param.sql(), param.argus())) {
            statement.setFetchSize(size);
            List<T> results = new ArrayList<>(size);
            RowMapper<T> rowMapper = (RowMapper<T>) RowMapperFactory.create(param.reuiredClass());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (size == 1 && !results.isEmpty()) {
                    throw new DataAccessException("Multiple row");
                }
                T rowObject = rowMapper.mapRow(resultSet, resultSet.getRow());
                if (rowObject != null) {
                    results.add(rowObject);
                }
            }
            return results;
        } catch (SQLException e) {
            throw new DataAccessException("SQL is wrong");
        }
    }

    private PreparedStatement createStatement(Connection connection, String sql, List<Object> argus) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < argus.size(); i++) {
                statement.setObject(i + 1, argus.get(i));
            }
            return statement;
        } catch (SQLException e) {
            throw new DataAccessException("SQL is wrong");
        }
    }

}
