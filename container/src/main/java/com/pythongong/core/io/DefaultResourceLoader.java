package com.pythongong.core.io;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.pythongong.util.PathUtils;


public class DefaultResourceLoader implements ResourceLoader {

    @Override
    public Resource getResource(String location) {
        
        if (location.startsWith(PathUtils.CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location);
        }

        try {
            URL url = new URI(location).toURL();
            return new RemoteResource(url);
        } catch (MalformedURLException | URISyntaxException e) {
            return new FileSystemResource(location);
        }


    }
    
}
