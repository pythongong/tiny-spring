package com.pythongong.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.exception.BeansException;
import com.pythongong.io.DefaultResourceLoader;
import com.pythongong.io.Resource;
import com.pythongong.io.ResourceLoader;

public class ContextUtils {

    /**
     * Creates and initializes a PropertyResolver by scanning for and loading
     * all .properties files in the classpath.
     *
     * @return configured PropertyResolver instance
     */
    public static PropertyResolver createPropertyResolver() {
        List<String> propertiesFiles = new ArrayList<>(4);
        List<String> yamlFiles = new ArrayList<>(4);
        FileUtils.findClassPathFileNames(ClassPathSerchParam.builder()
                .packagePath(FileUtils.ROOT_CLASS_PATH)
                .searchSudDirect(false)
                .serachJar(false)
                .serachFile(true)
                .pathMapper((basePath, filePath) -> {
                    String fileName = FileUtils.CLASSPATH_URL_PREFIX + filePath.getFileName().toString();
                    if (fileName.endsWith(FileUtils.PROPERTY_SUFFIX)) {
                        propertiesFiles.add(fileName);
                    }
                    if (fileName.endsWith(FileUtils.YAML_SUFFIX)) {
                        yamlFiles.add(fileName);
                    }
                })
                .build());

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        PropertyResolver propertyResolver = new PropertyResolver();
        propertiesFiles.forEach(propertiesFile -> {
            Resource resource = resourceLoader.getResource(propertiesFile);
            try {
                propertyResolver.load(resource.getInputStream());
            } catch (IOException e) {
                throw new BeansException(String.format("Load propeties file {%s} failed", propertiesFile), e);
            }
        });
        yamlFiles.forEach(yamlFile -> {
            Map<String, Object> yamlData = FileUtils.loadYaml(yamlFile);
            propertyResolver.addAll(yamlData);
        });
        return propertyResolver;
    }
}
