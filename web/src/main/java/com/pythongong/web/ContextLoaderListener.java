package com.pythongong.web;

import com.pythongong.context.ApplicationContext;
import com.pythongong.context.impl.AnnotationConfigApplicationContext;
import com.pythongong.exception.WebException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRegistration.Dynamic;

public class ContextLoaderListener implements ServletContextListener {

    private static final String APP_CONTEXT = "applicationContext";

    private static final String APP_SERVLET = "dispatcherServlet";
    private static final String DEFAULT_MAPPING = "/";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String encoding = "UTF-8";
        ServletContext servletContext = sce.getServletContext();
        servletContext.setRequestCharacterEncoding(encoding);
        servletContext.setResponseCharacterEncoding(encoding);
        ApplicationContext applicationContext = createAppContext(servletContext);
        resgisterDispatcher(servletContext, applicationContext);
        servletContext.setAttribute(APP_CONTEXT, applicationContext);
    }

    private void resgisterDispatcher(ServletContext servletContext, ApplicationContext applicationContext) {
        DispatcherServlet servlet = new DispatcherServlet(applicationContext);
        Dynamic dispatcherReg = servletContext.addServlet(APP_SERVLET, servlet);
        dispatcherReg.addMapping(DEFAULT_MAPPING);
        dispatcherReg.setLoadOnStartup(0);
    }

    private ApplicationContext createAppContext(ServletContext servletContext) {
        String configClassName = servletContext.getInitParameter("configuration");
        try {
            Class<?> configClass = Class.forName(configClassName);
            return new AnnotationConfigApplicationContext(configClass);
        } catch (ClassNotFoundException e) {
            throw new WebException("Can not find config class");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Object appContext = servletContext.getAttribute(APP_CONTEXT);
        if (appContext instanceof ApplicationContext) {
            ((ApplicationContext) appContext).close();
        }
    }

}
