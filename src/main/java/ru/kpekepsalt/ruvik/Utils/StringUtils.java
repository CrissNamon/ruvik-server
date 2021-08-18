package ru.kpekepsalt.ruvik.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtils {
    /**
     * @param object Object to convert
     * @return JSON string representation of Object
     * @throws JsonProcessingException Raised if Object can't be converted
     */
    public static String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
