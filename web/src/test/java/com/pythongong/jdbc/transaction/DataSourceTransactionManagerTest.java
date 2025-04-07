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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pythongong.aop.interceptor.AdviceInvocation;
import com.pythongong.enums.TransactionIsolationLevel;
import com.pythongong.exception.AopConfigException;

class DataSourceTransactionManagerTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private AdviceInvocation invocation;

    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
        transactionManager = new DataSourceTransactionManager(dataSource, TransactionIsolationLevel.READ_COMMITTED);
    }

    @Test
    void testSuccessfulTransaction() throws Throwable {
        // Setup
        when(invocation.proceed()).thenReturn("result");

        // Execute
        Object result = transactionManager.invoke(invocation);

        // Verify
        assertEquals("result", result);
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(TransactionIsolationLevel.READ_COMMITTED.getLevel());
        verify(connection).commit();
        verify(connection).setAutoCommit(true);
    }

    @Test
    void testTransactionRollback() throws Throwable {
        // Setup
        when(invocation.proceed()).thenThrow(new AopConfigException("Test exception"));

        // Execute and verify
        assertThrows(AopConfigException.class, () -> transactionManager.invoke(invocation));

        verify(connection).setAutoCommit(false);
        verify(connection).rollback();
        verify(connection).setAutoCommit(true);
    }

    @Test
    void testNestedTransaction() throws Throwable {
        // Setup a flag to prevent infinite recursion
        final boolean[] isNested = { false };

        when(invocation.proceed()).thenAnswer(inv -> {
            if (isNested[0]) {
                // Verify connection is available
                assertNotNull(DataSourceTransactionManager.getConnection());
                // Return a value for nested transaction
                return "nested result";
            }
            isNested[0] = true;
            return transactionManager.invoke(invocation);
        });

        // Execute
        Object result = transactionManager.invoke(invocation);

        // Verify
        assertEquals("nested result", result);
        // Verify connection operations happened only once
        verify(connection, times(1)).setAutoCommit(false);
        verify(connection, times(1)).commit();
    }

    @Test
    void testConnectionFailure() throws SQLException {
        // Setup
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Execute and verify
        assertThrows(AopConfigException.class, () -> transactionManager.invoke(invocation));
    }

    @Test
    void testRollbackFailure() throws Throwable {
        // Setup
        when(invocation.proceed()).thenThrow(new AopConfigException("Business exception"));
        doThrow(new SQLException("Rollback failed")).when(connection).rollback();

        // Execute and verify
        AopConfigException exception = assertThrows(AopConfigException.class,
                () -> transactionManager.invoke(invocation));
        assertEquals("rollback failed", exception.getMessage());
    }

    @Test
    void testConcurrentTransactions() throws Throwable {
        // Setup different connections for different threads
        Connection connection2 = mock(Connection.class);
        when(dataSource.getConnection())
                .thenReturn(connection) // First thread
                .thenReturn(connection2); // Second thread

        // Create a latch to synchronize threads
        CountDownLatch startLatch = new CountDownLatch(2);
        CountDownLatch endLatch = new CountDownLatch(2);

        // Create two threads with different transactions
        Runnable transaction1 = () -> {
            try {
                startLatch.countDown();
                startLatch.await(); // Wait for both threads to start
                transactionManager.invoke(invocation);
                endLatch.countDown();
            } catch (Exception e) {
                fail("Transaction 1 failed: " + e.getMessage());
            }
        };

        Runnable transaction2 = () -> {
            try {
                startLatch.countDown();
                startLatch.await(); // Wait for both threads to start
                transactionManager.invoke(invocation);
                endLatch.countDown();
            } catch (Exception e) {
                fail("Transaction 2 failed: " + e.getMessage());
            }
        };

        // Execute concurrent transactions
        Thread thread1 = new Thread(transaction1, "Transaction-1");
        Thread thread2 = new Thread(transaction2, "Transaction-2");

        thread1.start();
        thread2.start();

        // Wait for both transactions to complete
        endLatch.await(5, TimeUnit.SECONDS);

        // Verify each connection was used correctly
        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection2).setAutoCommit(false);
        verify(connection2).commit();
    }

    @Test
    void testThreadLocalCleanup() throws Throwable {
        // Execute a transaction and verify ThreadLocal is cleaned up
        transactionManager.invoke(invocation);
        assertNull(DataSourceTransactionManager.getConnection());
    }

    @Test
    void testThreadLocalIsolation() throws Throwable {
        // Setup different connections for different threads
        Connection connection2 = mock(Connection.class);
        when(dataSource.getConnection())
                .thenReturn(connection)
                .thenReturn(connection2);

        CompletableFuture<Connection> thread1Connection = new CompletableFuture<>();
        CompletableFuture<Connection> thread2Connection = new CompletableFuture<>();

        // Configure invocation to capture connections
        when(invocation.proceed()).thenAnswer(inv -> {
            Thread currentThread = Thread.currentThread();
            if ("Thread-1".equals(currentThread.getName())) {
                thread1Connection.complete(DataSourceTransactionManager.getConnection());
                thread2Connection.get(); // Wait for thread 2
            } else {
                thread2Connection.complete(DataSourceTransactionManager.getConnection());
            }
            return null;
        });

        // Run transactions in different threads
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Thread-1");
            try {
                transactionManager.invoke(invocation);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("Thread-2");
            try {
                transactionManager.invoke(invocation);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

        // Wait for both transactions to complete
        CompletableFuture.allOf(future1, future2).get(5, TimeUnit.SECONDS);

        // Verify each thread got its own connection
        assertNotNull(thread1Connection.get());
        assertNotNull(thread2Connection.get());
        assertNotSame(thread1Connection.get(), thread2Connection.get());
    }
}