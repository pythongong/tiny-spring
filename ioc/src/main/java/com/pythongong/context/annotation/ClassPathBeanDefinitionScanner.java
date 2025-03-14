package com.pythongong.context.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.core.filter.AnnotationTypeFilter;
import com.pythongong.core.filter.TypeFilter;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.PathUtils;

public class ClassPathBeanDefinitionScanner {
    
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private final List<TypeFilter> includeFilters;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this(beanDefinitionRegistry, null);
    }


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry, List<TypeFilter> includeFilters) {
        this.beanDefinitionRegistry = beanDefinitionRegistry == null ? new DefaultListableBeanFactory() : beanDefinitionRegistry;
        this.includeFilters = includeFilters == null ? new ArrayList<>() : includeFilters;
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }

    /**
	 * Add an include type filter to the <i>end</i> of the inclusion list.
	 */
	public void addIncludeFilter(TypeFilter includeFilter) {
		this.includeFilters.add(includeFilter);
	}

    public Set<BeanDefinition> scan(String... basePackages) {
        if (basePackages == null) {
            throw new IllegalArgumentException("At least one package");
        }

        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanCandidateComponents(basePackage);
            for (BeanDefinition candiate : candidates) {
                beanDefinitionRegistry.registerBeanDefinition(candiate.beanClass().getName(), candiate);
            }
            beanDefinitions.addAll(candidates);
        }
        return beanDefinitions;
    }

    private Set<BeanDefinition> scanCandidateComponents(String basePackage) {
        Set<BeanDefinition> beanDefinitions = new HashSet<>();

        Set<String> classNames = PathUtils.getFileNamesOfPackage(basePackage, fileName -> {
            if (fileName.endsWith(PathUtils.CLASS_FILE_SUFFIX)) {
                fileName= fileName.substring(0, fileName.length() - PathUtils.CLASS_FILE_SUFFIX.length());
                return fileName.replace(PathUtils.SYSTEM_PATH_SEPARATOR, PathUtils.PACKAGE_SEPARATOR);
            }
            return null;
        });

        classNames.forEach(className -> {
            try {
                if (className == null) {
                    return;
                }
                Class<?> clazz = Class.forName(className);
                if (!isCandidateComponent(clazz)) {
                    return;
                }
                checkModifiers(clazz);
                // init method:
                Method initMethod = ClassUtils.findInitOrDestoryMethod(clazz, PostConstruct.class);
                // destroy method:
                Method destoryMethod = ClassUtils.findInitOrDestoryMethod(clazz, PreDestroy.class);
                
                beanDefinitions.add(new BeanDefinition(clazz, null, initMethod,  destoryMethod));
            } catch (ClassNotFoundException e) {
                throw new BeansException("package path: " + basePackage, e);
            }
        });
        return beanDefinitions;
    }

    private boolean isCandidateComponent(Class<?> clazz) {
        for (TypeFilter typeFilter : includeFilters) {
            if (typeFilter.match(clazz)) {
                return true;
            }
            
        }
        return false;
    }
    
    private void checkModifiers(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isPrivate(modifiers) || Modifier.isAbstract(modifiers)) {
            throw new BeansException("modifer is private or abstract");
        }
    }

}
