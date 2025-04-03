package com.pythongong.restful;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pythongong.annotation.PathVariable;
import com.pythongong.annotation.RequestParam;
import com.pythongong.annotation.ResponseBody;
import com.pythongong.enums.ParamType;
import com.pythongong.exception.WebException;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;
import com.pythongong.utils.JsonUtils;
import com.pythongong.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class Dispatcher {

    private static final Result NOT_PROCESSED = new Result(false, null);

    private final boolean isPost;
    private final boolean returnBody;
    private final boolean returnVoid;
    private final Object controller;
    private final Method method;
    private final List<Param> params;
    private final Pattern urlPattern;

    public Dispatcher(boolean isPost, Object controller, Method method, String url) {
        this.isPost = isPost;
        this.controller = controller;
        this.method = method;
        returnBody = method.isAnnotationPresent(ResponseBody.class);
        returnVoid = method.getReturnType() == Void.class;
        urlPattern = WebUtils.generatePathPattern(url);

        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[] annotations = method.getAnnotations();
        params = createParams(parameterTypes, annotations);
    }

    private List<Param> createParams(Class<?>[] parameterTypes, Annotation[] annotations) {
        if (ClassUtils.isArrayEmpty(parameterTypes) || ClassUtils.isArrayEmpty(annotations)) {
            return null;
        }

        return Arrays.stream(parameterTypes).map(paramClass -> {
            ParamType paramType = ParamType.getTypeFromAnnos(annotations);
            if (paramType == null) {
                throw new WebException("");
            }
            String name = "";
            switch (paramType) {
                case REQUEST_PARAM: {
                    RequestParam requestParam = method.getAnnotation(RequestParam.class);
                    name = requestParam.value();
                    break;
                }
                case PATH_VARIABLE: {
                    PathVariable pathVariable = method.getAnnotation(PathVariable.class);
                    name = pathVariable.value();
                    break;
                }
                default:
                    break;
            }

            return new Param(paramType, paramClass, name);
        }).toList();
    }

    public Result process(HttpServletRequest req, HttpServletResponse resp) {
        String url = req.getRequestURI();
        Matcher matcher = urlPattern.matcher(url);
        if (!matcher.matches()) {
            return NOT_PROCESSED;
        }
        if (ClassUtils.isCollectionEmpty(params)) {
            return invokeHandlerMethod(null);
        }
        Object[] arguments = params.stream()
                .map(param -> {
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

        return

        invokeHandlerMethod(arguments);
    }

    private Result invokeHandlerMethod(Object[] argus) {
        try {
            Object retVal = method.invoke(controller, argus);
            return new Result(true, retVal);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new WebException("");
        }
    }

}
