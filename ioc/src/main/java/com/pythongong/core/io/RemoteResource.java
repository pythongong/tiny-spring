package com.pythongong.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class RemoteResource implements Resource {

    private final URL url;

    public RemoteResource(URL url) {
        this.url = url;
    }



    @Override
    public InputStream getInputStream() throws IOException{
        URLConnection connection = url.openConnection();
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection)connection).disconnect();
            }
            throw e;
        }
        
    }
    
}
