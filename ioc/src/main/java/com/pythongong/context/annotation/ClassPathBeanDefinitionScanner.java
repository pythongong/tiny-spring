package com.pythongong.context.annotation;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.TypeElement;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.core.filter.AnnotationTypeFilter;
import com.pythongong.core.filter.TypeFilter;
import com.pythongong.exception.IocException;
import com.pythongong.stereotype.Component;
import com.pythongong.util.PathUtils;

public class ClassPathBeanDefinitionScanner {
    
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private final List<TypeFilter> includeFilters = new ArrayList<>();


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }

    public void scan(String... basePackages) {
        doScan(basePackages);
    }

    /**
	 * Add an include type filter to the <i>end</i> of the inclusion list.
	 */
	public void addIncludeFilter(TypeFilter includeFilter) {
		this.includeFilters.add(includeFilter);
	}

    private void doScan(String... basePackages) {
        if (basePackages == null) {
            throw new IllegalArgumentException("At least one package");
        }
        for (String basePackage : basePackages) {
            List<BeanDefinition> candidates = scanCandidateComponents(basePackage);
            for (BeanDefinition candiate : candidates) {
                beanDefinitionRegistry.registerBeanDefinition(candiate.getBeanClass().getName(), candiate);
            }
        }
    }

    private List<BeanDefinition> scanCandidateComponents(String basePackage) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

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
                beanDefinitions.add(new BeanDefinition(clazz));
            } catch (ClassNotFoundException e) {
                throw new IocException("package path: " + basePackage, e);
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
            throw new IocException("modifer is private or abstract");
        }
    }

}
