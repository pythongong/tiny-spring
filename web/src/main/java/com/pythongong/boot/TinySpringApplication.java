package com.pythongong.boot;

import java.util.Set;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.exception.WebException;
import com.pythongong.restful.ContextLoaderInitialize;
import com.pythongong.util.ContextUtils;

public class TinySpringApplication {

    private static final String BASE_DIR = "target/classes";

    public static void run(Class<?> configClass, Object... args) {
        new TinySpringApplication().start(configClass, args);
    }

    public Server start(Class<?> configClass, Object... args) {
        PropertyResolver propertyResolver = ContextUtils.createPropertyResolver();
        int port = (int) propertyResolver.getProperty("${server.port:8080}", int.class);
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        String path = configClass.getPackageName();
        Context context = tomcat.addWebapp(BASE_DIR, path);
        WebResourceRoot webResourceRoot = new StandardRoot(context);
        webResourceRoot.addPreResources(new DirResourceSet(webResourceRoot, "", path, "/"));
        context.setResources(webResourceRoot);
        context.addServletContainerInitializer(new ContextLoaderInitialize(configClass, propertyResolver), Set.of());
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new WebException("");
        }
        return tomcat.getServer();
    }
}
