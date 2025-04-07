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

package com.pythongong.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pythongong.enums.TransactionIsolationLevel;

/**
 * Annotation that marks a method or class as transactional.
 * 
 * <p>When applied to a method, makes that specific method execute within
 * a transaction. When applied to a class, makes all methods in that class
 * transactional by default.
 *
 * <p>Example usage:
 * <pre>
 * {@code @Transactional(level = TransactionIsolationLevel.READ_COMMITTED)}
 * public void transferMoney(Account from, Account to, BigDecimal amount) {
 *     // ... transaction code ...
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Transactional {
    
    /**
     * The name of the transaction manager to use.
     * Defaults to "platformTransactionManager".
     *
     * @return the transaction manager bean name
     */
    String value() default "platformTransactionManager";

    /**
     * The transaction isolation level.
     * Defaults to TransactionIsolationLevel.NONE.
     *
     * @return the transaction isolation level
     */
    TransactionIsolationLevel level() default TransactionIsolationLevel.NONE;
}
