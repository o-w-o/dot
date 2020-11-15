package o.w.o.server.runner;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.repository.AuthorizationStubRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 应用启动任务
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Slf4j
@Component
public class ApplicationBootstrapRunner implements ApplicationRunner {

  @Resource
  private AuthorizationStubRepository authorizationStubRepository;

  private void resetAuthorizationStubRepository() {
    logger.info("ApplicationRunner: [RUN] 清除令牌");
    authorizationStubRepository.deleteAll();
    logger.info("ApplicationRunner: [RUN] 清除令牌，END");
  }


  @Override
  public void run(ApplicationArguments args) {
    logger.info("ApplicationRunner: [START]");

    resetAuthorizationStubRepository();

    logger.info("ApplicationRunner: [END]");
  }
}
