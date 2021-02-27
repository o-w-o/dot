package o.w.o.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocketConfiguration
 *
 * @author symbols@dingtalk.com
 * @date 2020/03/09
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfiguration {
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}
