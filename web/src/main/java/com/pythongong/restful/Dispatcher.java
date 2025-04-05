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

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pythongong.annotation.PathVariable;
import com.pythongong.annotation.RequestBody;
import com.pythongong.annotation.RequestParam;
import com.pythongong.annotation.ResponseBody;
import com.pythongong.enums.ParamType;
import com.pythongong.exception.WebException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;
import com.pythongong.utils.JsonUtils;
import com.pythongong.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * Handles the dispatching of HTTP requests to controller methods.
 * 
 * <p>
 * Maps incoming HTTP requests to the appropriate controller methods based on
 * URL patterns
 * and HTTP methods. Supports parameter resolution for path variables, request
 * parameters,
 * and request body.
 *
 * @author pythongong
 * @since 1.0
 */
@Getter
public class Dispatcher {

    /** Result indicating the request was not processed */
    private static final Result NOT_PROCESSED = new Result(false, null);

    /** Whether the method returns a response body */
    private final boolean returnBody;
    /** Whether the method returns void */
    private final boolean returnVoid;
    /** The controller instance */
    private final Object controller;
    /** The handler method */
    private final Method method;
    /** The method parameters */
    private final List<Param> params;
    /** The URL pattern for matching requests */
    private final Pattern urlPattern;

    /**
     * Creates a new dispatcher for the given controller method.
     * 
     * @param isPost     whether this handles POST requests
     * @param controller the controller instance
     * @param method     the handler method
     * @param url        the URL pattern to match
     */
    public Dispatcher(Object controller, Method method, String url) {
        String className = "Dispatcher";
        CheckUtils.nullArgs(controller, className, "controller");
        CheckUtils.nullArgs(method, className, "method");
        CheckUtils.emptyString(url, className, "url");

        this.controller = controller;
        this.method = method;
        returnBody = method.isAnnotationPresent(ResponseBody.class);
        returnVoid = method.getReturnType() == Void.class;
        urlPattern = WebUtils.generatePathPattern(url);

        params = createParams();
    }

    

    /**
     * Processes an HTTP request.
     * 
     * @param req  the HTTP request
     * @param resp the HTTP response
     * @return the result of processing
     * @throws WebException if processing fails
     */
    public Result process(HttpServletRequest req, HttpServletResponse resp) {
        String url = req.getRequestURI();
        Matcher matcher = urlPattern.matcher(url);
        if (!matcher.matches()) {
            return NOT_PROCESSED;
        }
        if (ClassUtils.isCollectionEmpty(params)) {
            return invokeHandlerMethod(null);
        }
        Object[] arguments = createArgus(req, matcher);

        return invokeHandlerMethod(arguments);
    }

    /**
     * Creates parameter descriptors for the method parameters.
     * 
     * @return list of parameter descriptors
     * @throws WebException if parameter configuration is invalid
     */
    private List<Param> createParams() {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ClassUtils.isArrayEmpty(parameterTypes) || ClassUtils.isArrayEmpty(parameterTypes)) {
            return null;
        }
        List<Param> params = new ArrayList<>(parameterTypes.length);
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramClass = parameterTypes[i];
            ParamType paramType = null;    
            String name = null;

            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestParam) {
                    paramType = ParamType.REQUEST_PARAM;
                    String value = ((RequestParam) annotation).value();
                    if (StringUtils.isEmpty(value)) {
                        throw new WebException("");
                    }
                    name = value;
                    break;    
                }
            
                if (annotation instanceof PathVariable) {
                    paramType = ParamType.PATH_VARIABLE;
                    String value = ((PathVariable) annotation).value();
                    if (StringUtils.isEmpty(value)) {
                        throw new WebException("");
                    }
                    name = value;
                    break;
                }
                if (annotation instanceof RequestBody) {
                    paramType = ParamType.REQUEST_BODY;
                    break;
                }
            }
            if (paramType == null) {
                continue;       
            }
            params.add(new Param(paramType, paramClass, name));
        }
        return params;
    }

    /**
     * Creates method arguments for the given request and matcher.
     * 
     * @param req  the HTTP request
     * @param matcher the URL pattern matcher
     * @return array of method arguments
     */
    private Object[] createArgus(HttpServletRequest req, Matcher matcher) {
        return params.stream().map(param -> {
            switch (param.paramType()) {
                case REQUEST_BODY: {
                    try {
                        BufferedReader reader = req.getReader();
                        return JsonUtils.readJson(reader, param.classType());
                    } catch (IOException e) {
                        throw new WebException("");
                    }
                }

                case REQUEST_PARAM: {
                    String paramVal = req.getParameter(param.name());
                    return StringUtils.convertString(paramVal, param.classType());
                }

                case PATH_VARIABLE: {
                    String paramVal = matcher.group(param.name());
                    return StringUtils.convertString(paramVal, param.classType());
                }

                default:
                    throw new WebException("");
            }
        }).toArray();
    }

    /**
     * Invokes the handler method with the given arguments.
     * 
     * @param argus the method arguments
     * @return the result of invocation
     * @throws WebException if invocation fails
     */
    private Result invokeHandlerMethod(Object[] argus) {
        try {
            Object retVal = method.invoke(controller, argus);
            return new Result(true, retVal);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new WebException("");
        }
    }
}
