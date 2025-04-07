package com.pythongong.boot;

import java.io.File;
import java.util.Set;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.exception.WebException;
import com.pythongong.restful.ContextLoaderInitialize;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ContextUtils;
import com.pythongong.util.StringUtils;
import com.pythongong.utils.WebUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TinySpringApplication {

    private static final String BASE_DIR = "/target/classes";

    private File tempBaseDir;

    public static void run(Class<?> configClass, Object... args) {
        CheckUtils.nullArgs(configClass, "TinySpringApplication.run", "configClass");
        new TinySpringApplication().start(configClass, args);
    }

    public void start(Class<?> configClass, Object... args) {
        Server server = startTomcat(configClass);
        try {
            server.await();
        } finally {
            close(server);
        }

    }

    Server startTomcat(Class<?> configClass) {
        PropertyResolver propertyResolver = ContextUtils.createPropertyResolver();

        int port = (int) propertyResolver.getProperty("${server.port:8080}", int.class);
        if (port < 0) {
            throw new WebException("Invalid port number");
        }
        Tomcat tomcat = new Tomcat();
        String classDir = WebUtils.getConfigPath(configClass);
        if (StringUtils.isEmpty(classDir)) {
            throw new WebException("Empty config path");
        }
        tempBaseDir = new File(classDir, "standalone-tomcat-temp");
        tempBaseDir.mkdir();
        tomcat.setBaseDir(tempBaseDir.getAbsolutePath());

        Context context = tomcat.addContext(BASE_DIR, classDir);
        context.addServletContainerInitializer(new ContextLoaderInitialize(configClass, propertyResolver), Set.of());

        tomcat.setPort(port);
        tomcat.getConnector();

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new WebException(String.format("Failed to start Tomcat server on port {%d} for class {%s}", port,
                    configClass.getCanonicalName()));
        }
        return tomcat.getServer();
    }

    void close(Server server) {
        log.info("Boot destory starts");
        if (server != null) {
            log.info("Stop Tomcat");
            try {
                server.stop();
                server.destroy();
            } catch (LifecycleException e) {
                throw new WebException("");
            }
        }

        if (tempBaseDir != null && tempBaseDir.exists()) {
            log.info("Delete temp directory");
            boolean deleteDirectory = WebUtils.deleteDirectory(tempBaseDir);
            log.info("Delete reslut: " + deleteDirectory);
        }
    }
}
