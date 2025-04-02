package com.pythongong.jdbc;

import java.util.List;

import lombok.Builder;

@Builder
public record QueryParam(String sql, Class<?> reuiredClass, List<Object> argus) {

}
