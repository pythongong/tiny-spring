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

package com.pythongong.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.pythongong.aop.interceptor.AdviceInvocation;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.enums.TransactionIsolationLevel;
import com.pythongong.exception.AopConfigException;

/**
 * Transaction manager that manages JDBC transactions through a DataSource.
 * 
 * <p>This implementation supports transaction management using JDBC connections
 * obtained from a DataSource. It handles transaction isolation levels and
 * provides thread-safe transaction management using ThreadLocal storage.
 *
 * <p>Implements both PlatformTransactionManager for transaction management and
 * MethodInterceptor for AOP-based transaction handling.
 *
 * @author Cheng Gong
 * @since 1.0
 */
public class DataSourceTransactionManager implements PlatformTransactionManager, MethodInterceptor {

    /** ThreadLocal storage for transaction status */
    private static final ThreadLocal<TransactionStatus> LOCAL_STATUS = new ThreadLocal<>();

    /** The transaction isolation level */
    private final int isolationLevel;

    /** The DataSource to obtain connections from */
    private final DataSource dataSource;

    /**
     * Creates a new transaction manager with specified DataSource and isolation level.
     *
     * @param dataSource the DataSource to obtain connections from
     * @param level the transaction isolation level
     */
    public DataSourceTransactionManager(DataSource dataSource, TransactionIsolationLevel level) {
        this.dataSource = dataSource;
        isolationLevel = level.getLevel();
    }

    /**
     * Gets the current transaction's connection.
     *
     * @return the current Connection or null if no transaction is active
     */
    public static Connection getConnection() {
        TransactionStatus transactionStatus = LOCAL_STATUS.get();
        if (transactionStatus == null) {
            return null;
        }
        return transactionStatus.connection();
    }

    /**
     * Handles method interception for transactional operations.
     * 
     * <p>Implements REQUIRED propagation behavior - uses existing transaction
     * if present, creates new one if none exists.
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the method invocation
     * @throws AopConfigException if transaction handling fails
     */
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

                    throw new AopConfigException(String.format("Transaction rollback failed for method {%s} in class {%s}" 
            , invocation.method().getName(), invocation.target().getClass().getCanonicalName()));
                }
                throw new AopConfigException(String.format("Perform transaction failed for method {%s} in class {%s}" 
            , invocation.method().getName(), invocation.target().getClass().getCanonicalName()));
            } finally {
                LOCAL_STATUS.remove();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new AopConfigException(String.format("Get transactional connection failed for method {%s} in class {%s}" 
            , invocation.method().getName(), invocation.target().getClass().getCanonicalName()));
        }
    }
}
