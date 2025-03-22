package com.pythongong.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a parameter, return value, or field may be null.
 * This annotation is used for documentation purposes and can be helpful
 * for static code analysis tools.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Nullable {
}
