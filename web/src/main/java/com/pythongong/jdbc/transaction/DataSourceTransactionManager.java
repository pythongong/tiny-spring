package com.pythongong.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.pythongong.aop.interceptor.AdviceInvocation;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.enums.TransactionIsolationLevel;
import com.pythongong.exception.AopConfigException;

public class DataSourceTransactionManager implements PlatformTransactionManager, MethodInterceptor {

    private static final ThreadLocal<TransactionStatus> LOCAL_STATUS = new ThreadLocal<>();

    private final int isolationLevel;

    private final DataSource dataSource;

    public DataSourceTransactionManager(DataSource dataSource, TransactionIsolationLevel level) {
        this.dataSource = dataSource;
        isolationLevel = level.getLevel();
    }

    public static Connection getConnection() {
        TransactionStatus transactionStatus = LOCAL_STATUS.get();
        if (transactionStatus == null) {
            return null;
        }
        return transactionStatus.connection();
    }

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        TransactionStatus transactionStatus = LOCAL_STATUS.get();
        // REQUIRED propagation
        // If there's an outer transaction, just joint it
        if (transactionStatus != null) {
            return invocation.proceed();
        }

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(isolationLevel);
            try {
                LOCAL_STATUS.set(new TransactionStatus(connection));
                Object result = invocation.proceed();
                connection.commit();
                return result;
            } catch (AopConfigException | SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new AopConfigException("rollback failed");
                }
                throw new AopConfigException("perfrom transaction failed");
            } finally {
                LOCAL_STATUS.remove();
                connection.setAutoCommit(true);

            }
        } catch (SQLException e) {
            throw new AopConfigException("");
        }

    }

}
