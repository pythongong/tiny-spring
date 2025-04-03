package com.pythongong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pythongong.exception.WebException;

public class JsonUtils {

    private JsonUtils() {
    }

    public static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    static {
        DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        DEFAULT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DEFAULT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        DEFAULT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static void writeJson(PrintWriter writer, Object obj) {
        try {
            DEFAULT_MAPPER.writeValue(writer, obj);
        } catch (IOException e) {
            throw new WebException("");
        }
    }

    public static Object readJson(BufferedReader reader, Class<?> classType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readJson'");
    }

}
