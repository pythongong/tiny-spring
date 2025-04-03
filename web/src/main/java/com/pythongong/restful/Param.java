package com.pythongong.restful;

import com.pythongong.enums.ParamType;

public record Param(ParamType paramType, Class<?> classType, String name) {

}
