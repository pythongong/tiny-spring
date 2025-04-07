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
import lombok.Builder;

/**
 * Parameter holder for database queries.
 * 
 * <p>This record encapsulates all necessary parameters for executing a SQL query,
 * including the SQL statement, the required result type, and any query arguments.
 * It uses the Builder pattern for flexible construction.
 *
 * <p>Example usage:
 * <pre>
 * QueryParam param = QueryParam.builder()
 *     .sql("SELECT * FROM users WHERE id = ?")
 *     .reuiredClass(User.class)
 *     .argus(List.of(userId))
 *     .build();
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Builder
public record QueryParam(
        /** The SQL query string */
        String sql,
        
        /** The required result type class */
        Class<?> reuiredClass,
        
        /** The list of query arguments */
        List<Object> argus) {

    /**
     * Validates the query parameters during construction.
     *
     * @throws IllegalArgumentException if sql is null
     */
    public QueryParam {
        CheckUtils.nullArgs(sql, "QueryParam", "sql");
    }
}
