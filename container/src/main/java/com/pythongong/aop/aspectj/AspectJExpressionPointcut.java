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
package com.pythongong.aop.aspectj;

import java.lang.reflect.Method;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;

import com.pythongong.util.CheckUtils;

/**
 * 
 * This class uses AspectJ's pointcut expression language to determine
 * which methods should be intercepted.
 * 
 * <p>
 * Supports the full AspectJ pointcut expression syntax, including:
 * <ul>
 * <li>execution() expressions for method execution matching</li>
 * <li>within() expressions for type matching</li>
 * <li>args() expressions for argument matching</li>
 * <li>@annotation expressions for annotation matching</li>
 * </ul>
 * 
 * <p>
 * Example expressions:
 * <ul>
 * <li>"execution(* com.example.service.*.*(..))" - matches all methods in
 * service package</li>
 * <li>"execution(public * *(..))" - matches all public methods</li>
 * <li>"execution(@Transactional * *(..))" - matches annotated methods</li>
 * </ul>
 *
 * @author Cheng Gong
 * @see MethodMatcher
 * @see org.aspectj.weaver.tools.PointcutExpression
 */
public class AspectJExpressionPointcut implements MethodMatcher {

    /**
     * The compiled AspectJ pointcut expression used for method matching.
     */
    private final PointcutExpression pointcutExpression;

    /**
     * Creates a new pointcut using the specified AspectJ expression.
     *
     * @param expression the AspectJ pointcut expression
     * @throws IllegalArgumentException                if the expression is null or
     *                                                 empty
     * @throws org.aspectj.weaver.tools.ParseException if the expression is invalid
     */
    public AspectJExpressionPointcut(String expression) {
        CheckUtils.emptyString(expression, "AspectJ expression must not be empty");
        PointcutParser pointcutParser = PointcutParser
                .getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * Tests whether the given method matches this pointcut.
     *
     * @param method the method to check
     * @return true if the method matches the pointcut expression
     */
    @Override
    public boolean matches(Method method) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    public boolean matchesClass(Class<?> targetClass) {
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    public boolean isDynamic() {
        return pointcutExpression.mayNeedDynamicTest();
    }

    public MethodMatcher methodMatcher() {
        return this;
    }

}
