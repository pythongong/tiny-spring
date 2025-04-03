package com.pythongong.restful;

import java.util.List;
import java.util.function.Supplier;

import jakarta.servlet.Filter;

public class FilterRegistrationBean {

    public final Supplier<List<String>> getUrlPatterns;

    public final Supplier<Filter> getFilter;

    public FilterRegistrationBean(Supplier<List<String>> getUrlPatterns, Supplier<Filter> getFilter) {
        this.getUrlPatterns = getUrlPatterns;
        this.getFilter = getFilter;
    }

    public String getName() {
        return getClass().getSimpleName();
    };

}
