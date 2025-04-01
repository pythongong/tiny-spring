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

package com.pythongong.enums;

import java.lang.annotation.Annotation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Around;

/**
 * Enumeration of supported AspectJ advice types.
 * 
 * <p>Provides mapping between AspectJ annotations and their corresponding
 * enum representations, along with utility methods for advice handling.
 *
 * @author pythongong
 * @since 1.0
 */
public enum AdviceEnum {
    /** Represents @Before advice */
    Before(Before.class),
    /** Represents @After advice */
    After(After.class),
    /** Represents @AfterReturning advice */
    AfterReturning(AfterReturning.class),
    /** Represents @Around advice */
    Around(Around.class);

    /** The actual annotation class that this enum constant represents */
    private final Class<? extends Annotation> annotationClass;

    /**
     * Constructs an AdviceEnum with its corresponding annotation class.
     * @param annotationClass the annotation class this enum represents
     */
    private AdviceEnum(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * Gets the enum constant corresponding to the given annotation class.
     * @param annotationClass the annotation class to look up
     * @return the corresponding AdviceEnum constant, or null if not found
     */
    public static AdviceEnum fromAnnotation(Class<? extends Annotation> annotationClass) {
        for (AdviceEnum type : values()) {
            if (type.annotationClass.equals(annotationClass)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Compares two advice types based on their execution order priority.
     * @param enum1 the first advice enum to compare
     * @param enum2 the second advice enum to compare
     * @return negative if enum1 should execute before enum2, positive if after, zero if equal
     */
    public static int compare(AdviceEnum enum1, AdviceEnum enum2) {
        if (enum1 == enum2) {
            return 0;
        }
        if (enum1 == Before) {
            return -1;
        }
        if (enum2 == Before) {
            return 1;
        }
        if (enum1 == Around) {
            return -1;
        }
        if (enum2 == Around) {
            return 1;
        }
        if (enum1 == After) {
            return -1;
        }

        return 1;
    }
}
