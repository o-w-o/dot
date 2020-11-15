package o.w.o;

import o.w.o.util.ContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * 程序入口
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/10
 * @since 1.0.0
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
  public static void main(String[] args) {
    ContextUtil.initialApplicationContext(
        SpringApplication.run(Application.class, args)
    );
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }
}
