package com.pythongong.aop.aspectj;

import java.lang.reflect.Method;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;

import com.pythongong.aop.MethodMatcher;
import com.pythongong.util.CheckUtils;

public class AspectJExpressionPointcut implements MethodMatcher {

    private final PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        CheckUtils.emptyString(expression, "AspectJ expression must not be empty");
        PointcutParser pointcutParser = PointcutParser
                .getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }
}
