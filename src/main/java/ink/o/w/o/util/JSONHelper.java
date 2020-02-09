package ink.o.w.o.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 下午5:11
 */
@Component
public class JSONHelper {
    private ObjectMapper objectMapper;

    public JSONHelper() {
        init();
    }

    private void init() {
        this.objectMapper = new ObjectMapper();

        objectMapper.registerModule(new Hibernate5Module());

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public  String toJSONString(Object obj) throws JsonProcessingException {
        return this.toJSONString(obj, this.getObjectMapper());
    }

    public  String toJSONString(Object obj, ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json string convert to map
     *
     * @return
     */
    public  <T> Map toMap(String jsonStr) throws IOException {
        return this.getObjectMapper().readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public  <T> Map<String, T> toMap(String jsonStr, Class<T> clazz) throws IOException {
        Map<String, Map<String, Object>> map = this.getObjectMapper().readValue(jsonStr, new TypeReference<Map<String, Map<String, Object>>>() {
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
    public  <T> List<T> toList(String jsonArrayStr, Class<T> clazz) throws IOException {
        List<Map<String, Object>> list = this.getObjectMapper().readValue(jsonArrayStr, new TypeReference<List>() {
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
    public  <T> T toObject(Map map, Class<T> clazz) {
        return this.getObjectMapper().convertValue(map, clazz);
    }

    /**
     * json string convert to javaBean
     */
    public  <T> T toObject(String jsonStr, Class<T> clazz) throws Exception {
        return this.getObjectMapper().readValue(jsonStr, clazz);
    }
}
