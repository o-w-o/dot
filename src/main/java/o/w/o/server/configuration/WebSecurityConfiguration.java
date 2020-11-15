package o.w.o.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@EnableWebSecurity
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
    // 基于 jwt，所以不需要 csrf、session
    httpSecurity
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // 配置 actuator
    httpSecurity
        .requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests(
        (requests) -> requests
            .antMatchers("*", endpointBaseUrl + "/**")
            .hasRole("ENDPOINT")
    );

    // 除网站静态资源无授权访问外，所有请求全部需要鉴权认证
    httpSecurity.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/static/resources/**").permitAll()
        .anyRequest().authenticated();

    // 禁用缓存
    httpSecurity.headers().cacheControl();

    // 设置 CSP 策略
    httpSecurity.headers().contentSecurityPolicy(config -> {
      config
          .policyDirectives("default-src 'self' https://www.o-w-o.store https://o-w-o.store")
          .policyDirectives("object-src 'none'")
          .policyDirectives("report-uri /report/csp/")
      ;
    });
  }
}
