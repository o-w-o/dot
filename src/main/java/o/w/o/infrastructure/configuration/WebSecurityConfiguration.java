package o.w.o.infrastructure.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 配置
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/2
 */
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${management.endpoints.web.base-path}")
  private String endpointBaseUrl;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    configureActuator(httpSecurity);

    httpSecurity.antMatcher("/api/**")
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    httpSecurity.antMatcher("/**")
        .cors()
        .and()
        .anonymous().disable()
        .headers()
        .contentSecurityPolicy(config -> config
            .policyDirectives(
                StringUtils.joinWith(
                    ";",
                    "default-src 'self' https://www.o-w-o.store https://o-w-o.store",
                    "object-src 'none'"
                )
            )
        )
        .contentTypeOptions().and()
        .xssProtection().and()
        .httpStrictTransportSecurity().and()
        .frameOptions()
    ;
  }

  private void configureActuator(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests(
        (requests) -> requests
            .antMatchers("*", endpointBaseUrl + "/**")
            .hasRole("ENDPOINT")
    );
  }
}
