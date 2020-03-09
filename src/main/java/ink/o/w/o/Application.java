package ink.o.w.o;

import ink.o.w.o.util.ContextHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 程序入口
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/10
 * @since 1.0.0
 */
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    ContextHelper.setApplicationContext(
        SpringApplication.run(Application.class, args)
    );
  }

}
