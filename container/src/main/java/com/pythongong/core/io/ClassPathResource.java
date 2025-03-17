package com.pythongong.core.io;

import java.io.IOException;
import java.io.InputStream;

import com.pythongong.util.ClassUtils;

public class ClassPathResource implements Resource {

    private String fileName;

    private ClassLoader classLoader;

    public ClassPathResource(String fileName) {
        this.fileName = fileName;
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return classLoader.getResourceAsStream(fileName);
    }
    
}
