package ink.o.w.o.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午11:45
 */

public class FastJsonConfiguration {
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);

        fastJsonHttpMessageConverter.setFeatures(
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.DisableCircularReferenceDetect
        );

        fastJsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        return fastJsonHttpMessageConverter;
    }
}
