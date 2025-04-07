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

package com.pythongong.restful;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pythongong.annotation.GetMapping;
import com.pythongong.annotation.PostMapping;
import com.pythongong.annotation.RestController;
import com.pythongong.context.ApplicationContext;
import com.pythongong.context.impl.AnnotationConfigApplicationContext;
import com.pythongong.exception.WebException;
import com.pythongong.util.ClassUtils;
import com.pythongong.utils.JsonUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Central dispatcher for HTTP web requests.
 * 
 * <p>Dispatches requests to registered handlers for processing. This servlet is the entry
 * point for the web framework's request processing infrastructure. It provides a request
 * processing workflow including handler mapping, request parameter resolution, and response
 * handling.
 *
 * <p>This implementation supports:
 * <ul>
 *   <li>REST controllers via {@code @RestController} annotation
 *   <li>GET requests via {@code @GetMapping} annotation
 *   <li>POST requests via {@code @PostMapping} annotation
 *   <li>Automatic JSON response serialization
 * </ul>
 *
 * @author pythongong
 * @since 1.0
 */
public class DispatcherServlet extends HttpServlet {

    /** Default content type for responses */
    private static final String DEFAULT_CONTENT = "application/json";

    /** The Spring application context */
    private final ApplicationContext applicationContext;

    /** Registered GET request dispatchers */
    private final List<Dispatcher> getDispatchers = new ArrayList<>(ClassUtils.SMALL_INIT_SIZE);

    /** Registered POST request dispatchers */
    private final List<Dispatcher> postDispatchers = new ArrayList<>(ClassUtils.SMALL_INIT_SIZE);

    /**
     * Creates a new dispatcher servlet with the given application context.
     *
     * @param applicationContext the Spring application context
     */
    public DispatcherServlet(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Initializes the servlet by scanning for REST controllers and creating dispatchers.
     * Finds all beans with @RestController annotation and registers their mapped methods.
     */
    @Override
    public void init() {
        Map<String, Object> concreteBeans = ((AnnotationConfigApplicationContext) applicationContext)
                .getBeansOfType(Object.class);
        concreteBeans.forEach((name, bean) -> {
            Class<? extends Object> beanClass = bean.getClass();
            if (!beanClass.isAnnotationPresent(RestController.class)) {
                return;
            }
            createDispatchers(bean, beanClass);

        });
    }

    /**
     * Cleans up resources by closing the application context.
     */
    @Override
    public void destroy() {
        applicationContext.close();
    }

   
    

    /**
     * Handles HTTP GET requests by delegating to registered GET dispatchers.
     *
     * @param req the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, getDispatchers);
    }

    /**
     * Handles HTTP POST requests by delegating to registered POST dispatchers.
     *
     * @param req the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, postDispatchers);
    }

     /**
     * Creates dispatcher entries for methods annotated with @GetMapping or @PostMapping.
     *
     * @param bean the controller bean instance
     * @param beanClass the class of the controller
     */
    private void createDispatchers(Object bean, Class<? extends Object> beanClass) {
        Arrays.stream(beanClass.getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(GetMapping.class)) {
                checkMethod(method);
                GetMapping get = method.getAnnotation(GetMapping.class);
                getDispatchers.add(new Dispatcher(bean, method, get.value()));
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                checkMethod(method);
                PostMapping post = method.getAnnotation(PostMapping.class);
                postDispatchers.add(new Dispatcher(bean, method, post.value()));
            }

        });
    }

    /**
     * Validates that the handler method is not static.
     *
     * @param method the method to check
     * @throws WebException if the method is static
     */
    private void checkMethod(Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new WebException(method.getName() + " is static");
        }
    }

    /**
     * Processes requests using the appropriate dispatchers.
     *
     * @param req the HTTP servlet request
     * @param resp the HTTP servlet response
     * @param dispatchers list of dispatchers to try
     */
    private void doService(HttpServletRequest req, HttpServletResponse resp, List<Dispatcher> dispatchers) {
        dispatchers.forEach(dispatcher -> {
            if (processReq(req, resp, dispatcher)) {
                return;
            }
        });
    }

    /**
     * Processes a single request with the given dispatcher.
     * Handles response writing and content type setting.
     *
     * @param req the HTTP servlet request
     * @param resp the HTTP servlet response
     * @param dispatcher the dispatcher to process the request
     * @return true if the request was processed, false otherwise
     * @throws WebException if response writing fails
     */
    private boolean processReq(HttpServletRequest req, HttpServletResponse resp, Dispatcher dispatcher) {
        Result result = dispatcher.process(req, resp);
        if (!result.isProcessed()) {
            return false;
        }
        if (!resp.isCommitted()) {
            resp.setContentType(DEFAULT_CONTENT);
        }

        try {
            PrintWriter writer = resp.getWriter();
            if (!dispatcher.isReturnBody()) {
                if (dispatcher.isReturnVoid()) {
                    JsonUtils.writeJson(writer, resp);
                    writer.flush();
                }
                return true;
            }
            Object retVal = result.retVal();
            if (retVal instanceof String str) {
                writer.write(str);
                writer.flush();
            } else if (retVal instanceof Object) {
                ServletOutputStream outputStream = resp.getOutputStream();
                outputStream.write(JsonUtils.toJsonBytes(retVal));
                outputStream.flush();
            } else {
                throw new WebException("");
            }
            return true;
        } catch (IOException e) {
            throw new WebException("");
        }

    }

    
}
