package com.pythongong.beans.config;

import com.pythongong.util.CheckUtils;

public record FactpryDefinition(/*
                                 * The factory name to be used for creating the bean.
                                 * May be null, in which case the bean class will be used.
                                 */
        String factoryName,

        /*
         * The factory method to be used for creating the bean.
         * May be null, in which case the default constructor will be used.
         */
        String factoryMethodName,
        Class<?>[] factoryMethodParamTypes) {

    public FactpryDefinition {
        CheckUtils.emptyString(factoryName, "FactpryDefinition recevies empty factory name");
        CheckUtils.emptyString(factoryMethodName, "FactpryDefinition recevies empty factory method name");
    }

}
