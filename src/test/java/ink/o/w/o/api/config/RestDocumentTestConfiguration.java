package ink.o.w.o.api.config;

import ink.o.w.o.server.domain.AuthorizedJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

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
      configurer.snippets().withEncoding("GBK");
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
          preprocessResponse(prettyPrint()),
          requestHeaders(
              headerWithName(AuthorizedJwt.AUTHORIZATION_HEADER_KEY).description(String.format("认证字段，格式： `%s + jwt`", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX)).optional()
          ),
          responseHeaders(
              headerWithName("Content-Type").description("载荷格式, e.g. `application/hal+json`")
          )
      );
    }
  }
}
