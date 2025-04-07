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

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import com.pythongong.beans.impl.InstantiationStrategy;
import com.pythongong.beans.impl.SimpleInstantiation;
import com.pythongong.exception.DataAccessException;

/**
 * Factory for creating {@link RowMapper} instances.
 * 
 * <p>This factory provides built-in mappers for common types and a default mapper
 * that uses reflection to map database columns to object fields. It maintains a
 * cache of common mappers for better performance.
 *
 * <p>Supported built-in types:
 * <ul>
 *   <li>Boolean
 *   <li>String
 * </ul>
 *
 * @author Cheng Gong
 * @since 1.0
 */
public class RowMapperFactory {

    /** Cache of pre-configured row mappers */
    public static final Map<Class<?>, RowMapper<?>> rowMapperMap = new HashMap<>(2);

    /** Initialize built-in mappers */
    static {
        rowMapperMap.put(Boolean.class, (resultSet, rowNum) -> {
            return resultSet.getBoolean(rowNum);
        });
        rowMapperMap.put(String.class, (resultSet, rowNum) -> {
            return resultSet.getString(rowNum);
        });
    }

    /**
     * Creates a row mapper for the specified target class.
     *
     * @param targetClass the class to create a mapper for
     * @return a row mapper for the target class
     */
    public static RowMapper<?> create(Class<?> targetClass) {
        RowMapper<?> rowMapper = rowMapperMap.get(targetClass);
        if (rowMapper == null) {
            return defaultRowMapper(targetClass);
        }
        return rowMapper;
    }

    /**
     * Creates a default row mapper using reflection.
     *
     * @param <T> the target type
     * @param targetClass the class to create a mapper for
     * @return a row mapper that maps columns to fields by name
     */
    @SuppressWarnings("unchecked")
    private static <T> RowMapper<T> defaultRowMapper(Class<T> targetClass) {
        InstantiationStrategy instantiationStrategy = new SimpleInstantiation();
        T instance = (T) instantiationStrategy.instance(targetClass, null, null);
        return (resultSet, rowNum) -> {
            ResultSetMetaData metaData = resultSet.getMetaData();
            if (metaData == null) {
                throw new DataAccessException("");
            }
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                try {
                    String label = metaData.getColumnLabel(rowNum);
                    Field field = targetClass.getDeclaredField(label);
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(label));
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                        | IllegalAccessException e) {
                    throw new DataAccessException("No such coulumn");
                }
            }
            return instance;
        };
    }
}
