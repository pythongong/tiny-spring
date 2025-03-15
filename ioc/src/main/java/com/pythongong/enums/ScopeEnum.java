package com.pythongong.enums;

import com.pythongong.util.StringUtils;

public enum ScopeEnum {
    SINGLETON("singleton"), PROTOTYPE("prototype");

    private final String scope;

    ScopeEnum(String scope) {
        this.scope = scope;
    }

    public static ScopeEnum fromScope(String scope) {
        for (ScopeEnum scopeEnum : values()) {
            if (scopeEnum.scope.equals(scope)) {
                return scopeEnum;
            }
        }
        if (!StringUtils.isEmpty(scope)) {
            throw new IllegalArgumentException("Illega scope: " + scope);
        }
        return SINGLETON;
    }

}
