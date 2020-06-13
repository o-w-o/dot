package o.w.o.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * AutoConfigureRestDocs 自动配置参考 https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured-rest-docs
 * Asciidoctor 语法参考 https://asciidoctor.org/docs/asciidoc-syntax-quick-reference/
 */
@Slf4j
@TestConfiguration
public class RestDocumentTestConfiguration {
  @TestConfiguration
  public static class MyRestDocsMockMvcConfiguration implements RestDocsMockMvcConfigurationCustomizer {

    @Override
    public void customize(MockMvcRestDocumentationConfigurer configurer) {
      logger.info("RestDocumentTestConfiguration:RestDocsMockMvcConfiguration loading ……");

      // 使用 @AutoConfigureRestDocs 注解时，需要再次手动指定 uris 的以下属性
      configurer.uris()
          .withScheme("https")
          .withHost("api.o-w-o.ink")
          .withPort(443);

      // 中文处理时需指定编码为 GBK，否则 asciidoctor 处理会报异常！
      configurer.snippets().withEncoding(UTF_8);
    }
  }

  @TestConfiguration(proxyBeanMethods = false)
  public static class MyRestDocumentationConfiguration {

    @Bean
    public RestDocumentationResultHandler restDocumentation() {
      logger.info("RestDocumentTestConfiguration:RestDocumentationResultHandler loading ……");

      return MockMvcRestDocumentation.document(
          "{ClassName}--{method-name}",
          preprocessRequest(prettyPrint()),
          preprocessResponse(prettyPrint())
      );
    }
  }
}
