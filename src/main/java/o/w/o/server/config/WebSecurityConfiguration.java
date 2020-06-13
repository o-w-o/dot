package o.w.o.server.config;

import o.w.o.server.controller.AccessDeniedController;
import o.w.o.server.controller.UnAuthorizedAccessController;
import o.w.o.server.filter.AuthorityInjector;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Spring Security 配置
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/2 下午11:07
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${management.endpoints.web.base-path}")
  private String endpointBaseUrl;

  @Resource
  private UnAuthorizedAccessController unAuthorizedAccessController;

  @Resource
  private AccessDeniedController accessDeniedController;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthorityInjector authenticationTokenFilterBean() {
    return new AuthorityInjector();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity

        // 由于使用的是JWT，我们这里不需要csrf
        .csrf().disable()

        // 基于token，所以不需要session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // 配置 actuator
    httpSecurity
        .requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests(
        (requests) -> requests
            .antMatchers("*", endpointBaseUrl + "/**")
            .hasRole("ENDPOINT")
    );


    httpSecurity.authorizeRequests()

        // 允许对于网站静态资源的无授权访问
        .antMatchers(HttpMethod.GET, "/static/resources/**").permitAll()

        // 除上面外的所有请求全部需要鉴权认证
        .anyRequest().authenticated();


    httpSecurity
        .exceptionHandling()
        .authenticationEntryPoint(unAuthorizedAccessController)
        .accessDeniedHandler(accessDeniedController);

    // 添加 JWT filter
    httpSecurity
        .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

    // 禁用缓存
    httpSecurity.headers().cacheControl();
  }
}
