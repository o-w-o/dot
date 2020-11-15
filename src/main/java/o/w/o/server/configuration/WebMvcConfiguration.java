package o.w.o.server.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置
 *
 * @author symbols@dingtalk.com
 * @apiNote 参考 [SpringBoot 中 WebMvcConfigurer 中可配置项全解](https://blog.csdn.net/turbo_zone/article/details/84401890)
 * @date 2020/11/14
 */
@Slf4j
@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
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

    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000")
        .allowedMethods("GET", "POST", "DELETE", "PATCH", "OPTIONS")
        .allowCredentials(true)
        .allowedHeaders("*");
  }

}
