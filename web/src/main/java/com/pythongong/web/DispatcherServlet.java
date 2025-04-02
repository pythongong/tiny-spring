package com.pythongong.web;

import java.io.IOException;
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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

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
                getDispatchers.add(Dispatcher.builder()
                        .controller(bean)
                        .isPost(false)
                        .method(method)
                        .url(get.value())
                        .build());
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                checkMethod(method);
                PostMapping post = method.getAnnotation(PostMapping.class);
                postDispatchers.add(Dispatcher.builder()
                        .controller(bean)
                        .isPost(true)
                        .method(method)
                        .url(post.value())
                        .build());
            }

        });
    }

    private void checkMethod(Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new WebException(method.getName() + " is static");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        applicationContext.close();
    }

}
