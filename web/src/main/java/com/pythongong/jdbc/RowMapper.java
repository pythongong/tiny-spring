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

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pythongong.stereotype.Nullable;

/**
 * An interface for mapping rows of a {@link java.sql.ResultSet} on a per-row basis.
 * 
 * <p>Implementations of this interface perform the actual work of mapping each row
 * to a result object. RowMapper objects are typically stateless and thus reusable;
 * they are ideal for mapping results on a per-row basis.
 *
 * <p>Example usage:
 * <pre>
 * RowMapper<User> mapper = (rs, rowNum) -> {
 *     User user = new User();
 *     user.setId(rs.getLong("id"));
 *     user.setName(rs.getString("name"));
 *     return user;
 * };
 * </pre>
 *
 * @param <T> the result type
 * @author Cheng Gong
 * @since 1.0
 */
@FunctionalInterface
public interface RowMapper<T> {

    /**
     * Implementations must implement this method to map each row of data in the
     * ResultSet.
     *
     * @param resultSet the ResultSet to map
     * @param rowNum the number of the current row
     * @return the result object for the current row, or null if row should be skipped
     * @throws SQLException if an SQLException is encountered getting column values
     */
    @Nullable
    T mapRow(ResultSet resultSet, int rowNum) throws SQLException;
}
