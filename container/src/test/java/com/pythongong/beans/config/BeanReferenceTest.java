
/*
 * Copyright (C) 2023 [Your Name or Organization]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pythongong.beans.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for BeanReference.
 * Verifies the basic functionality of BeanReference including creation,
 * equality comparison, and hash code generation.
 */
class BeanReferenceTest {

    /**
     * Tests the creation of a BeanReference and verifies that the bean name
     * is correctly stored and accessible.
     */
    @Test
    void testBeanReferenceCreation() {
        String beanName = "testBean";
        BeanReference reference = new BeanReference(beanName);
        
        assertEquals(beanName, reference.beanName(), "Bean name should match the constructor argument");
    }

    /**
     * Tests the equality comparison between BeanReference instances.
     * Verifies that references with the same name are considered equal,
     * while references with different names are not.
     */
    @Test 
    void testBeanReferenceEquality() {
        BeanReference ref1 = new BeanReference("testBean");
        BeanReference ref2 = new BeanReference("testBean");
        BeanReference ref3 = new BeanReference("differentBean");

        assertEquals(ref1, ref2, "BeanReferences with same name should be equal");
        assertNotEquals(ref1, ref3, "BeanReferences with different names should not be equal");
    }

    /**
     * Tests the hash code generation for BeanReference instances.
     * Verifies that references with the same name generate the same hash code.
     */
    @Test
    void testBeanReferenceHashCode() {
        BeanReference ref1 = new BeanReference("testBean");
        BeanReference ref2 = new BeanReference("testBean");
        
        assertEquals(ref1.hashCode(), ref2.hashCode(), 
            "Hash codes should be equal for BeanReferences with same name");
    }
}