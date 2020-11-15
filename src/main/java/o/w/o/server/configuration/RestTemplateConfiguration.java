package o.w.o.server.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfiguration
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/14
 */
@Configuration
public class RestTemplateConfiguration {
  @Bean
  @ConditionalOnMissingBean({RestOperations.class, RestTemplate.class})
  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
    return new RestTemplate(factory);
  }

  @Bean
  @ConditionalOnMissingBean({ClientHttpRequestFactory.class})
  public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setReadTimeout(15000);
    factory.setConnectTimeout(15000);
    return factory;
  }
}
