package com.pythongong.enums;

import java.lang.annotation.Annotation;

import com.pythongong.annotation.PathVariable;
import com.pythongong.annotation.RequestBody;
import com.pythongong.annotation.RequestParam;

public enum ParamType {
    PATH_VARIABLE(PathVariable.class), REQUEST_PARAM(RequestParam.class), REQUEST_BODY(RequestBody.class);

    private final Class<? extends Annotation> anonClass;

    private ParamType(Class<? extends Annotation> anonClass) {
        this.anonClass = anonClass;
    }

    public static ParamType getTypeFromAnnos(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            for (ParamType paramType : values()) {
                if (paramType.anonClass == annotation.annotationType()) {
                    return paramType;
                }
            }
        }

        return null;
    }

}
