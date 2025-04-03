package com.pythongong.utils;

import java.util.regex.Pattern;

import com.pythongong.context.ApplicationContext;
import com.pythongong.exception.WebException;
import com.pythongong.restful.DispatcherServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;

public class WebUtils {

    private static final String APP_SERVLET = "dispatcherServlet";
    private static final String DEFAULT_MAPPING = "/";

    private WebUtils() {
    }

    public static void resgisterDispatcher(ServletContext servletContext, ApplicationContext applicationContext) {
        DispatcherServlet servlet = new DispatcherServlet(applicationContext);
        Dynamic dispatcherReg = servletContext.addServlet(APP_SERVLET, servlet);
        dispatcherReg.addMapping(DEFAULT_MAPPING);
        dispatcherReg.setLoadOnStartup(0);
    }

    public static Pattern generatePathPattern(String url) {
        String regPath = url.replaceAll("\\{([a-zA-Z][a-zA-Z0-9]*)\\}", "(?<$1>[^/]*)");
        if (regPath.indexOf('{') >= 0 || regPath.indexOf('}') >= 0) {
            throw new WebException("Invalid path: " + url);
        }
        return Pattern.compile("^" + regPath + "$");
    }

}
