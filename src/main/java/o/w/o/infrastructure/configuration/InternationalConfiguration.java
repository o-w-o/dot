package o.w.o.infrastructure.configuration;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
/**
 * 国际化配置
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/14
 */
@Configuration
public class InternationalConfiguration {

  /**
   * 自定义 Validator 国际化文件存放路径
   *
   * @return -
   */
  @Bean
  public Validator getValidator() {
    return Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("i18n/message")))
        .buildValidatorFactory()
        .getValidator();
  }
}
