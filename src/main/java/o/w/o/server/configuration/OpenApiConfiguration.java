package o.w.o.server.configuration;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * OpenApiConfiguration
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/8
 */
@Configuration
@EnableOpenApi
public class OpenApiConfiguration {
  @Bean
  public Docket defaultDocket() {
    return new Docket(DocumentationType.OAS_30)
        .groupName("default")
        .apiInfo(apiInfo())
        .enable(true)
        .select()
        .paths(PathSelectors.ant("/error").negate())
        .apis(RequestHandlerSelectors.withClassAnnotation(RequestMapping.class))
        .build()
        .forCodeGeneration(true)
        .securitySchemes(securitySchemes())
        .securityContexts(singletonList(securityContext()));
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("烛火录")
        .description("接口文档 —— 基于 REST 和 OpenApi 3.0 接口约定 和 JWT 接口认证，通过 Springfox 生成。")
        .license("Apache 2.0")
        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
        .version("1.0.0")
        .build();
  }

  private List<SecurityScheme> securitySchemes() {
    return List.of(
        new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION + "(Token 前缀 [JWT.])", In.HEADER.name())
    );
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(
            List.of(
                defaultSecurityReference()
            )
        )
        .build();
  }

  private SecurityReference defaultSecurityReference() {
    return
        new SecurityReference(
            HttpHeaders.AUTHORIZATION,
            new AuthorizationScope[]{new AuthorizationScope("global", "访问令牌")}
        );
  }

  @Bean
  protected UiConfiguration uiConfig() {
    return UiConfigurationBuilder
        .builder()
        .defaultModelRendering(ModelRendering.MODEL)
        .operationsSorter(OperationsSorter.ALPHA)
        .displayRequestDuration(true)
        .build();
  }

  @Slf4j
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE + 1000)
  public static class OperationCustomBuilderPlugin implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
      context
          .operationBuilder()
          .codegenMethodNameStem(context.getName());
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
      return documentationType.equals(DocumentationType.OAS_30);
    }
  }
}
