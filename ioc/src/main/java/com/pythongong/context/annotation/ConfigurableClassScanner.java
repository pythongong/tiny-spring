package com.pythongong.context.annotation;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pythongong.core.filter.AnnotationTypeFilter;
import com.pythongong.core.filter.TypeFilter;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;
import com.pythongong.util.PathUtils;

public class ConfigurableClassScanner {
    
    private final List<TypeFilter> includeFilters;

    public ConfigurableClassScanner() {
        this(null);
    }


    public ConfigurableClassScanner(List<TypeFilter> includeFilters) {
        this.includeFilters = includeFilters == null ? new ArrayList<>() : includeFilters;
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }

    /**
	 * Add an include type filter to the <i>end</i> of the inclusion list.
	 */
	public void addIncludeFilter(TypeFilter includeFilter) {
		this.includeFilters.add(includeFilter);
	}

    public Set<Class<?>> scan(String... basePackages) {
        if (basePackages == null) {
            throw new IllegalArgumentException("At least one package");
        }

        Set<Class<?>> beanClasses = new HashSet<>();
        for (String basePackage : basePackages) {
            beanClasses.addAll(scanCandidateComponents(basePackage));
        }
        return beanClasses;
    }

    private Set<Class<?>> scanCandidateComponents(String basePackage) {
        Set<String> classNames = PathUtils.getFileNamesOfPackage(basePackage, fileName -> {
            if (fileName.endsWith(PathUtils.CLASS_FILE_SUFFIX)) {
                fileName= fileName.substring(0, fileName.length() - PathUtils.CLASS_FILE_SUFFIX.length());
                return fileName.replace(PathUtils.SYSTEM_PATH_SEPARATOR, PathUtils.PACKAGE_SEPARATOR);
            }
            return null;
        });

        Set<Class<?>> beanClasses = new HashSet<>();
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
                beanClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                throw new BeansException("package path: " + basePackage, e);
            }
        });
        return beanClasses;
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
