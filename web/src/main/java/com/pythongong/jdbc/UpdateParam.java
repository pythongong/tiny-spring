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

import java.util.List;
import com.pythongong.util.CheckUtils;

/**
 * Parameter holder for database update operations.
 * 
 * <p>This record encapsulates the SQL update statement and its arguments.
 * It is used for INSERT, UPDATE, and DELETE operations.
 *
 * <p>Example usage:
 * <pre>
 * UpdateParam param = new UpdateParam(
 *     "UPDATE users SET name = ? WHERE id = ?",
 *     List.of("John Doe", 1)
 * );
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
public record UpdateParam(
        /** The SQL update statement */
        String sql,
        
        /** The list of statement parameters */
        List<Object> argus) {

    /**
     * Validates the update parameters during construction.
     *
     * @throws IllegalArgumentException if sql is empty or null
     */
    public UpdateParam {
        CheckUtils.emptyString(sql, "UpdateParam", "sql");
    }
}
