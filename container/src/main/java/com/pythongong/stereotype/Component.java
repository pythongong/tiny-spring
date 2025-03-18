/*
 * Copyright 2025 Cheng Gong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pythongong.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a component.
 * Such classes will be detected during classpath scanning
 * and registered in the container as Spring beans.
 *
 * <p>Example usage:
 * <pre>{@code
 * @Component
 * public class UserService {
 *     // ...
 * }
 *
 * @Component("customerDao")
 * public class CustomerDaoImpl implements CustomerDao {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cheng Gong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Component {

    /**
     * The value may indicate a suggestion for a logical component name.
     * If not specified, the container will generate a name based on
     * the class name.
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
