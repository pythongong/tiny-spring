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
import com.pythongong.util.ClassPathSerchParam;
import com.pythongong.util.PathUtils;
import com.pythongong.util.StringUtils;

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
	 * Add an include type filter to  the inclusion list.
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
        Set<String> classNames = new HashSet<>();
        String packagePath = PathUtils.convertPackageToPath(basePackage);

        PathUtils.findClassPathFileNames(ClassPathSerchParam.builder()
        .packagePath(packagePath)
        .serachJar(true)
        .scanFile(true)
        .searchSudDirect(true)
        .pathMapper((basePath, filePath) -> {
            String filePathStr = filePath.toString();
            if (!filePathStr.endsWith(PathUtils.CLASS_FILE_SUFFIX)) {
                return;
            }
            String basePathStr = basePath.toString();
            // For Jar file system, it;s 0
            int startIndex = basePathStr.length() - packagePath.length();
            int endIndex = filePathStr.length() - PathUtils.CLASS_FILE_SUFFIX.length();
            String className = filePathStr.substring(startIndex, endIndex);
            className = className.replace(PathUtils.PATH_SEPARATOR, PathUtils.PACKAGE_SEPARATOR)
            .replace(PathUtils.SYSTEM_PATH_SEPARATOR, PathUtils.PACKAGE_SEPARATOR);
            if (!StringUtils.isEmpty(className)) {
                classNames.add(className);
            }
        })
        .build());

        Set<Class<?>> beanClasses = new HashSet<>();
        classNames.forEach(className -> {
            try {
                Class<?> clazz = Class.forName(className);
                if (isCandidateComponent(clazz)) {
                    beanClasses.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new BeansException("package path: " + basePackage, e);
            }
        });
        return beanClasses;
    }

    private boolean isCandidateComponent(Class<?> clazz) {
        for (TypeFilter typeFilter : includeFilters) {
            if (!typeFilter.match(clazz)) {
                continue;
            }
            int modifiers = clazz.getModifiers();
            if (Modifier.isPrivate(modifiers) || Modifier.isAbstract(modifiers)) {
                throw new BeansException("modifer is private or abstract");
            }
            return true;
        }
        return false;
    }
    
}
