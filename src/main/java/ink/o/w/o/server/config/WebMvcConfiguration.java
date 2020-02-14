package ink.o.w.o.server.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Spring MVC 配置
 *
 * @author symbols@dingtalk.com
 */
@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  private final HttpExceptionConfiguration handlerExceptionResolver;

  @Autowired
  WebMvcConfiguration(HttpExceptionConfiguration handlerExceptionResolver) {
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  /**
   * @param configurer -
   * @apiNote 参考 https://blog.csdn.net/turbo_zone/article/details/84401890
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseSuffixPatternMatch(false);
  }

  /**
   * 配置 JSON MessageConverter
   *
   * @param converters -
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter c = ((MappingJackson2HttpMessageConverter) converter);
        c.getObjectMapper()
            .registerModule(new Hibernate5Module())
            .registerModule(new Jdk8Module());
      } else if (converter instanceof StringHttpMessageConverter) {
        StringHttpMessageConverter c = ((StringHttpMessageConverter) converter);
        c.setDefaultCharset(StandardCharsets.UTF_8);
      } else {
        logger.info("未设置 converters 的类：" + converter.getClass().getName());
      }
    }
  }


  /**
   * 统一异常处理
   *
   * @param exceptionResolvers -
   */
  @Override
  public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    exceptionResolvers.add(handlerExceptionResolver);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/error").setViewName("error");
  }

  /**
   * 解决跨域问题 参考: http://www.cnblogs.com/520playboy/p/7306008.html
   * <br>注: 更细致的控制,可以使用 @CrossOrigin 注解在 controller 类中使用
   *
   * @param registry -
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("https://o-w-o.ink")
        .allowedMethods("GET", "POST", "DELETE", "PATCH", "OPTIONS")
        .allowCredentials(true)
        .allowedHeaders("*");
  }

}
