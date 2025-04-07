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

package com.pythongong.enums;

import java.sql.Connection;

import lombok.Getter;

/**
 * Enumeration of transaction isolation levels supported by JDBC.
 * 
 * <p>Maps to the standard JDBC Connection transaction isolation levels:
 * <ul>
 * <li>NONE - Transactions are not supported
 * <li>READ_UNCOMMITTED - Lowest isolation level, dirty reads are possible
 * <li>READ_COMMITTED - Prevents dirty reads
 * <li>REPEATABLE_READ - Prevents dirty reads and non-repeatable reads
 * <li>SERIALIZABLE - Highest isolation level, prevents all concurrency issues
 * </ul>
 *
 * @author Cheng Gong
 * @since 1.0
 * @see java.sql.Connection
 */
@Getter
public enum TransactionIsolationLevel {

    /** No transaction isolation */
    NONE(Connection.TRANSACTION_NONE),
    
    /** Read committed isolation level */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    
    /** Read uncommitted isolation level */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    
    /** Repeatable read isolation level */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    
    /** Serializable isolation level */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    /** The JDBC connection transaction isolation level value */
    private final int level;

    /**
     * Constructs a new TransactionIsolationLevel enum constant.
     *
     * @param level the JDBC connection transaction isolation level value
     */
    TransactionIsolationLevel(int level) {
        this.level = level;
    }
}
