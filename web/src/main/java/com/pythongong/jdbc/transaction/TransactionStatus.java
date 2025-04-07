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

import com.pythongong.util.CheckUtils;

/**
 * Represents the status of a transaction.
 * 
 * <p>This record holds the JDBC Connection associated with an active transaction.
 * It is used to track transaction state and ensure proper connection management
 * throughout the transaction lifecycle.
 *
 * @author pythongong
 * @since 1.0
 */
public record TransactionStatus(
        /** The JDBC connection associated with this transaction */
        Connection connection) {

    /**
     * Constructs a new transaction status with validation.
     *
     * @param connection the JDBC connection for this transaction
     * @throws IllegalArgumentException if connection is null
     */
    public TransactionStatus {
        CheckUtils.nullArgs(connection, "Connection must not be null");
    }
}
