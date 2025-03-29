package com.pythongong.aop;

import java.util.List;

import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;

public record AspectAdapter(String aspectName, List<AspectJExpressionPointcutAdvisor> advisors) {

}
