package ink.o.w.o.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;
import org.springframework.hateoas.support.WebStack;

@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL, stacks = WebStack.WEBMVC)
public class HateoasConfiguration {
  @Bean
  public CurieProvider curieProvider() {
    return new DefaultCurieProvider("ex", new UriTemplate("https://api.o-w-o.ink/rels/{rel}"));
  }
}
