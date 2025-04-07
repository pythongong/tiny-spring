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

public class JdbcTemplate {

    private static final int DEFAULT_SIZE = 10;

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    public int update(UpdateParam param) throws DataAccessException {
        return execute((connection) -> {
            try (PreparedStatement statement = createStatement(connection, param.sql(), param.argus())) {
                return statement.executeUpdate();
            }
        });
    }

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

    public <T> T queryOne(QueryParam param) throws DataAccessException {
        return execute((connection) -> {
            List<T> results = doQuery(connection, param, 1);
            if (ClassUtils.isCollectionEmpty(results)) {
                return null;
            }
            return results.iterator().next();

        });
    }

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
