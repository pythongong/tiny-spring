/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pythongong.boot;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
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

/**
 * Main entry point for launching a Spring application using embedded Tomcat.
 * 
 * <p>This class provides the infrastructure to start a Spring application with an
 * embedded Tomcat server. It handles the initialization of the application context,
 * property resolution, and server lifecycle management.
 *
 * <p>Example usage:
 * <pre>
 * {@code @Configuration}
 * public class Application {
 *     public static void main(String[] args) {
 *         TinySpringApplication.run(Application.class, args);
 *     }
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Slf4j
public class TinySpringApplication {

    /** Base directory for class files */
    private static final String BASE_DIR = "/target/classes";

    /** Temporary directory for Tomcat */
    private File tempBaseDir;

    /**
     * Static helper that can be used to run a {@link TinySpringApplication}
     * from the specified source.
     *
     * @param configClass the configuration class
     * @param args run arguments
     */
    public static void run(Class<?> configClass, Object... args) {
        CheckUtils.nullArgs(configClass, "TinySpringApplication.run", "configClass");
        new TinySpringApplication().start(configClass, args);
    }

    /**
     * Starts the application with the given configuration class.
     *
     * @param configClass the configuration class
     * @param args run arguments
     */
    public void start(Class<?> configClass, Object... args) {
        // start info:
        final long startTime = System.currentTimeMillis();
        final int javaVersion = Runtime.version().feature();
        final long pid = ManagementFactory.getRuntimeMXBean().getPid();
        final String user = System.getProperty("user.name");
        final String pwd = Paths.get("").toAbsolutePath().toString();
        log.info("Starting {} using Java {} with PID {} (started by {} in {})", configClass.getSimpleName(), javaVersion, pid, user, pwd);

        PropertyResolver propertyResolver = ContextUtils.createPropertyResolver();
        Server server = startTomcat(configClass, propertyResolver);

        // started info:
        final long endTime = System.currentTimeMillis();
        final String appTime = String.format("%.3f", (endTime - startTime) / 1000.0);
        final String jvmTime = String.format("%.3f", ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0);
        log.info("Started {} in {} seconds (process running for {})", configClass.getSimpleName(), appTime, jvmTime);
        
        try {
            server.await();
        } finally {
            close(server);
        }

    }

    /**
     * Starts an embedded Tomcat server.
     *
     * @param configClass the configuration class
     * @param propertyResolver resolver for configuration properties
     * @return the started Tomcat server instance
     * @throws WebException if server fails to start
     */
    Server startTomcat(Class<?> configClass, PropertyResolver propertyResolver) {
        int port = (int) propertyResolver.getProperty("${server.port:8080}", int.class);
        if (port < 0) {
            throw new WebException("Invalid port number: " + port);
        }
        Tomcat tomcat = new Tomcat();
        String classDir = WebUtils.getConfigPath(configClass);
        if (StringUtils.isEmpty(classDir)) {
            throw new WebException("Empty class path for class: " + configClass.getCanonicalName());
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

    /**
     * Closes the server and cleans up resources.
     *
     * @param server the server to stop
     * @throws WebException if server fails to stop
     */
    void close(Server server) {
        log.info("Boot destory process starts");
        if (server != null) {
            log.info("Stop Tomcat");
            try {
                server.stop();
                server.destroy();
                log.info("Stop Tomcat success");
            } catch (LifecycleException e) {
                throw new WebException("Stop Tomcat server failed");
            }
        }

        if (tempBaseDir != null && tempBaseDir.exists()) {
            log.info("Delete temp directory");
            boolean deleteDirectory = WebUtils.deleteDirectory(tempBaseDir);
            log.info("Delete reslut: " + deleteDirectory);
        }
    }
}
