package o.w.o.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import o.w.o.infrastructure.configuration.properties.constant.SystemOsName;
import o.w.o.infrastructure.configuration.properties.constant.SystemPropertiesDotPathConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * 环境配置
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/14
 */
@Slf4j
public class EnvironmentConfiguration implements EnvironmentPostProcessor {
  private Optional<PropertiesPropertySource> getRuntimePropertiesPropertySource() {
    return this.getRuntimePropertiesPropertySource("/etc/o-w-o/application-production.properties");
  }

  private Optional<PropertiesPropertySource> getRuntimePropertiesPropertySource(String propertySourcePath) {
    PropertiesPropertySource propertySource = null;

    try (FileInputStream fileInputStream = new FileInputStream(propertySourcePath)) {
      Properties properties = new Properties();
      properties.load(fileInputStream);

      propertySource = new PropertiesPropertySource("runtime", properties);
    } catch (Exception e) {
      logger.error("getRuntimePropertiesPropertySource Exception !", e);
    }

    return Optional.ofNullable(propertySource);
  }

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    String osName = System.getProperty(SystemPropertiesDotPathConstant.OS_NAME);

    for (PropertySource<?> ps : environment.getPropertySources()) {
      if (ps.getName().contains("systemProperties")) {
        logger.info("PropertySource [os.name] -> " + ps.getProperty("os.name"));
      }
      if (ps.getName().contains("config/application.properties")) {
        logger.info("PropertySource [spring.profiles.active] -> " + ps.getProperty("spring.profiles.active"));
      }
    }

    if (osName.contains(SystemOsName.Linux.getOsName())) {
      this.getRuntimePropertiesPropertySource().ifPresent(propertiesPropertySource -> environment
          .getPropertySources()
          .addLast(
              propertiesPropertySource
          ));
    } else {
      logger.info("当前系统非指定系统，跳过配置获取 -> " + osName);
    }
  }
}
