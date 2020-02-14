package ink.o.w.o.task;

import ink.o.w.o.resource.role.constant.Roles;
import ink.o.w.o.resource.role.util.RoleHelper;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.domain.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 初始化任务
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/11/28 11:21
 */

@Slf4j
@Component
@Configuration
@EnableScheduling
public class InitTask {
  private final static String MASTER_RANDOM_PASSWORD = UUID.randomUUID().toString();
  private final static String MASTER_NAME = "master";

  @Resource
  private UserService userService;

  @Value("${custom.env}")
  private String customEnv;
  @Value("${spring.profiles.active}")
  private String env;

  private void initAndCheckMasterUser() {
    logger.info("InitTask: customEnv -> {}, env -> {}", customEnv, env);
    logger.info("InitTask: initPassword -> {}, account -> {}", MASTER_RANDOM_PASSWORD, MASTER_NAME);

    ServiceResult<User> userServiceResult = userService.getUserByUsername(MASTER_NAME);
    if (userServiceResult.getSuccess()) {
      User u = userServiceResult.getPayload();

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      if (encoder.matches(MASTER_RANDOM_PASSWORD, u.getPassword()) || u.getCTime().equals(u.getUTime())) {
        logger.warn("请尽快修改管理员密码！cTime -> {}", u.getCTime());
        return;
      }

      logger.warn("检测到管理员密码已修改！cTime -> {}, uTime -> {}, isEqual ? {}", u.getCTime(), u.getCTime(), u.getCTime().equals(u.getUTime()));
      return;
    }

    userService.register(
        new User()
            .setName(MASTER_NAME)
            .setPassword(MASTER_RANDOM_PASSWORD)
            .setRoles(RoleHelper.toRoles(Roles.MASTER, Roles.USER))
    );
    System.err.println("执行静态定时任务 [ InitTask ] 时间: " + LocalDateTime.now());

  }

  @Scheduled(fixedRate = 1000 * 60 * 60)
  private void initAndCheckMasterUserTask() {
    initAndCheckMasterUser();
  }
}
