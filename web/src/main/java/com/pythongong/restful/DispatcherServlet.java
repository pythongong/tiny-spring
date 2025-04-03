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

public class DispatcherServlet extends HttpServlet {

    private static final String DEFAULT_CONTENT = "application/json";

    private final ApplicationContext applicationContext;

    private final List<Dispatcher> getDispatchers = new ArrayList<>(ClassUtils.SMALL_INIT_SIZE);

    private final List<Dispatcher> postDispatchers = new ArrayList<>(ClassUtils.SMALL_INIT_SIZE);

    public DispatcherServlet(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

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

    private void createDispatchers(Object bean, Class<? extends Object> beanClass) {
        Arrays.stream(beanClass.getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(GetMapping.class)) {
                checkMethod(method);
                GetMapping get = method.getAnnotation(GetMapping.class);
                getDispatchers.add(new Dispatcher(beanClass, method, get.value()));
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                checkMethod(method);
                PostMapping post = method.getAnnotation(PostMapping.class);
                postDispatchers.add(new Dispatcher(beanClass, method, post.value()));
            }

        });
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, getDispatchers);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, postDispatchers);
    }

    @Override
    public void destroy() {
        applicationContext.close();
    }

    private void checkMethod(Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new WebException(method.getName() + " is static");
        }
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp, List<Dispatcher> dispatchers) {
        dispatchers.forEach(dispatcher -> {
            if (processReq(req, resp, dispatcher)) {
                return;
            }
        });
    }

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
            } else if (retVal instanceof byte[] responseBody) {
                ServletOutputStream outputStream = resp.getOutputStream();
                outputStream.write(responseBody);
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
