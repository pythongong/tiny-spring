package com.pythongong.jdbc;

import java.util.List;

public record UpdateParam(String sql, List<Object> argus) {

}
