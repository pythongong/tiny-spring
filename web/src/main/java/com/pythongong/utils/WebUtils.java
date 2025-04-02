package com.pythongong.utils;

import com.pythongong.context.ApplicationContext;
import com.pythongong.web.DispatcherServlet;

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

}
