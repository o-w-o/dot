package ink.o.w.o.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.support.WebStack;

@Configuration
@EnableHypermediaSupport(type = HypermediaType.HAL_FORMS, stacks = WebStack.WEBMVC)
public class HateoasConfiguration {

}
