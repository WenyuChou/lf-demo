package com.example.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Map;

/**
 * json 工具类
 */
public class JsonUtil {
    private static ObjectMapper camelCaseMapper = createObjectMapper(PropertyNamingStrategy.LOWER_CAMEL_CASE);

    static ObjectMapper createObjectMapper(PropertyNamingStrategy strategy) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(strategy);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // disabled features:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // register module
        SimpleModule simpleModule = new SimpleModule();
        mapper.registerModule(simpleModule);
        return mapper;
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return camelCaseMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return camelCaseMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> readMapValue(String json) {
        return readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static String writeValueAsString(Object obj) {
        try {
            return camelCaseMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper getObjectMapper() {
        return camelCaseMapper;
    }
}
