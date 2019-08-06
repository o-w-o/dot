package ink.o.w.o.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 下午5:11
 */
public class JSONUtil {
    private static ObjectMapper defaultObjectMapper = JSONUtil.generateDefaultObjectMapper();

    public static ObjectMapper generateDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String toJSONString(Object obj) throws JsonProcessingException {
        return JSONUtil.toJSONString(obj, defaultObjectMapper);
    }

    public static String toJSONString(Object obj, ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) throws Exception {
        return defaultObjectMapper.readValue(jsonStr, clazz);
    }

    /**
     * json string convert to map
     *
     * @return
     */
    public static <T> Map toMap(String jsonStr) throws IOException {
        return defaultObjectMapper.readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> toMap(String jsonStr, Class<T> clazz) throws IOException {
        Map<String, Map<String, Object>> map = defaultObjectMapper.readValue(jsonStr, new TypeReference() {
        });
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), toObject(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> toList(String jsonArrayStr, Class<T> clazz) throws IOException {
        List<Map<String, Object>> list = defaultObjectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
        });
        List<T> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            result.add(toObject(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T toObject(Map map, Class<T> clazz) {
        return defaultObjectMapper.convertValue(map, clazz);
    }
}
