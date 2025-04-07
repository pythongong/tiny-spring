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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import com.pythongong.exception.DataAccessException;
import com.pythongong.mock.jdbc.TestUser;

class JdbcTemplateTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private RowMapper<TestUser> userRowMapper;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void testUpdate() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        UpdateParam param = new UpdateParam(
                "UPDATE users SET name = ? WHERE id = ?",
                Arrays.asList("John", 1));

        int result = jdbcTemplate.update(param);
        assertEquals(1, result);

        verify(preparedStatement).setObject(1, "John");
        verify(preparedStatement).setObject(2, 1);
    }

    @Test
    void testUpdateAndGetKey() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getObject(1)).thenReturn(1L);

        UpdateParam param = new UpdateParam(
                "INSERT INTO users (name) VALUES (?)",
                Arrays.asList("John"));

        Number key = jdbcTemplate.updateAndGetKey(param);
        assertEquals(1L, key.longValue());
    }

    @Test
    void testQueryOne() throws SQLException {

        try (MockedStatic<RowMapperFactory> mockedFactory = mockStatic(RowMapperFactory.class)) {
            TestUser testUser = new TestUser();
            testUser.setId(1);
            testUser.setName("John");

            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getRow()).thenReturn(1);
            when(userRowMapper.mapRow(resultSet, 1)).thenReturn(testUser);
            mockedFactory.when(() -> RowMapperFactory.create(TestUser.class)).thenReturn(userRowMapper);

            QueryParam param = QueryParam.builder()
                    .argus(Arrays.asList(1))
                    .sql("SELECT * FROM users WHERE id = ?")
                    .reuiredClass(TestUser.class).build();

            TestUser user = jdbcTemplate.queryOne(param);
            assertNotNull(user);
            assertEquals("John", user.getName());
            assertEquals(1, user.getId());
        }

    }

    @Test
    void testQueryList() throws SQLException {

        try (MockedStatic<RowMapperFactory> mockedFactory = mockStatic(RowMapperFactory.class)) {
            TestUser testUser = new TestUser();
            testUser.setId(1);
            testUser.setName("John");

            TestUser testUser2 = new TestUser();
            testUser.setId(2);
            testUser.setName("Jane");

            when(resultSet.next()).thenReturn(true, true, false);
            when(userRowMapper.mapRow(resultSet, 1)).thenReturn(testUser);
            when(userRowMapper.mapRow(resultSet, 2)).thenReturn(testUser2);
            when(resultSet.getRow()).thenReturn(1, 2);
            mockedFactory.when(() -> RowMapperFactory.create(TestUser.class)).thenReturn(userRowMapper);

            QueryParam param = QueryParam.builder()
                    .argus(List.of(List.of(1, 2)))
                    .sql("SELECT * FROM users")
                    .reuiredClass(TestUser.class)
                    .build();

            List<TestUser> users = jdbcTemplate.queryList(param);
            assertEquals(2, users.size());
        }

    }

    @Test
    void testExecuteWithException() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());

        UpdateParam param = new UpdateParam("SELECT 1", Arrays.asList());
        assertThrows(DataAccessException.class, () -> jdbcTemplate.update(param));
    }
}