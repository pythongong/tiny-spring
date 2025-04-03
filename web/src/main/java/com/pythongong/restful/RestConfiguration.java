package com.pythongong.restful;

import com.pythongong.stereotype.Bean;
import com.pythongong.stereotype.Configuration;

import jakarta.servlet.ServletContext;

@Configuration
public class RestConfiguration {
    private static ServletContext servletContext;

    public static void setServletContext(ServletContext context) {
        servletContext = context;
    }

    @Bean
    ServletContext servletContext() {
        return servletContext;
    }
}
