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

/**
 * Interface that defines the basic transaction operations.
 * 
 * <p>This is the central interface for transaction management in the framework.
 * Implementations can support different types of transaction management systems,
 * such as JTA, JDBC, Hibernate, etc.
 *
 * <p>Acts as a marker interface for transaction managers, allowing for
 * consistent transaction handling across different implementations.
 *
 * @author pythongong
 * @since 1.0
 */
public interface PlatformTransactionManager {

}
