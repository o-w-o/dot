package ink.o.w.o.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@Slf4j
public class EnvironmentConfiguration implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if(System.getProperty("os.name").contains("Linux")) {
            try {
                FileInputStream fileInputStream = new FileInputStream(
                    new File("/etc/o-w-o/application-production.properties")
                );

                Properties properties = new Properties();
                properties.load(fileInputStream);

                PropertiesPropertySource propertySource = new PropertiesPropertySource("my", properties);
                environment.getPropertySources().addLast(propertySource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
           logger.warn("当前系统非指定系统，跳过配置获取 -> {}", System.getProperty("os.name"));
        }
    }
}
