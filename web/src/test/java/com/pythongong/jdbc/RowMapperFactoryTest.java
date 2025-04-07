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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pythongong.exception.DataAccessException;
import com.pythongong.mock.jdbc.TestUser;

class RowMapperFactoryTest {

    @Mock
    private ResultSet resultSet;
    
    @Mock
    private ResultSetMetaData metaData;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(resultSet.getMetaData()).thenReturn(metaData);
    }

    @Test
    void testStringMapper() throws SQLException {
        when(resultSet.getString(1)).thenReturn("test value");
        
        RowMapper<?> mapper = RowMapperFactory.create(String.class);
        Object result = mapper.mapRow(resultSet, 1);
        
        assertEquals("test value", result);
    }

    @Test
    void testBooleanMapper() throws SQLException {
        when(resultSet.getBoolean(1)).thenReturn(true);
        
        RowMapper<?> mapper = RowMapperFactory.create(Boolean.class);
        Object result = mapper.mapRow(resultSet, 1);
        
        assertTrue((Boolean) result);
    }

    @Test
    void testCustomObjectMapper() throws SQLException {
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnLabel(1)).thenReturn("name");
        when(metaData.getColumnLabel(2)).thenReturn("age");
        when(resultSet.getObject("name")).thenReturn("John");
        when(resultSet.getObject("age")).thenReturn(30);
        
        RowMapper<?> mapper = RowMapperFactory.create(TestUser.class);
        TestUser result = (TestUser) mapper.mapRow(resultSet, 1);
        
        assertEquals("John", result.getName());
    }

    @Test
    void testMissingMetadata() throws SQLException {
        when(resultSet.getMetaData()).thenReturn(null);
        
        RowMapper<?> mapper = RowMapperFactory.create(TestUser.class);
        
        assertThrows(DataAccessException.class, () -> mapper.mapRow(resultSet, 1));
    }

    @Test
    void testInvalidColumnMapping() throws SQLException {
        when(metaData.getColumnCount()).thenReturn(1);
        when(metaData.getColumnLabel(1)).thenReturn("nonexistent");
        
        RowMapper<?> mapper = RowMapperFactory.create(TestUser.class);
        
        assertThrows(DataAccessException.class, () -> mapper.mapRow(resultSet, 1));
    }


}