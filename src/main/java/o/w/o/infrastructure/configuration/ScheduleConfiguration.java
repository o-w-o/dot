package o.w.o.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * ScheduleConfiguration
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/15
 */
@Configuration
@EnableScheduling
public class ScheduleConfiguration {
  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduling = new ThreadPoolTaskScheduler();
    scheduling.setPoolSize(10);
    scheduling.initialize();

    return scheduling;
  }
}
