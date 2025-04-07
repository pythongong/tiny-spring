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

package com.pythongong.restful;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.pythongong.enums.ParamType;
import com.pythongong.mock.jdbc.TestUser;

class ParamTest {

    @Test
    void testValidConstruction() {
        Param param = new Param(ParamType.REQUEST_PARAM, String.class, "testParam");

        assertEquals(ParamType.REQUEST_PARAM, param.paramType());
        assertEquals(String.class, param.classType());
        assertEquals("testParam", param.name());
    }

    @Test
    void testNullParamType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Param(null, String.class, "testParam");
        });
    }

    @Test
    void testNullClassType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Param(ParamType.REQUEST_PARAM, null, "testParam");
        });
    }

    @Test
    void testEquality() {
        Param param1 = new Param(ParamType.PATH_VARIABLE, Integer.class, "id");
        Param param2 = new Param(ParamType.PATH_VARIABLE, Integer.class, "id");
        Param param3 = new Param(ParamType.PATH_VARIABLE, String.class, "id");

        assertEquals(param1, param2);
        assertNotEquals(param1, param3);
    }

    @Test
    void testHashCode() {
        Param param1 = new Param(ParamType.REQUEST_BODY, TestUser.class, "user");
        Param param2 = new Param(ParamType.REQUEST_BODY, TestUser.class, "user");

        assertEquals(param1.hashCode(), param2.hashCode());
    }

}