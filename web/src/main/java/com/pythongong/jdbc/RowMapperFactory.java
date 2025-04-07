package com.pythongong.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import com.pythongong.beans.impl.InstantiationStrategy;
import com.pythongong.beans.impl.SimpleInstantiation;
import com.pythongong.exception.DataAccessException;

public class RowMapperFactory {

    public static final Map<Class<?>, RowMapper<?>> rowMapperMap = new HashMap<>(2);

    static {
        rowMapperMap.put(Boolean.class, (resultSet, rowNum) -> {
            return resultSet.getBoolean(rowNum);
        });
        rowMapperMap.put(String.class, (resultSet, rowNum) -> {
            return resultSet.getString(rowNum);
        });
    }

    public static RowMapper<?> create(Class<?> targetClass) {
        RowMapper<?> rowMapper = rowMapperMap.get(targetClass);
        if (rowMapper == null) {
            return defaultRowMapper(targetClass);
        }
        return rowMapper;
    }

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
