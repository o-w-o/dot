package ink.o.w.o.server.schedule;

import ink.o.w.o.resource.integration.email.service.MailService;
import ink.o.w.o.resource.system.role.constant.Roles;
import ink.o.w.o.resource.system.role.util.RoleHelper;
import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.resource.system.user.service.UserService;
import ink.o.w.o.server.config.properties.constant.SystemRuntimeEnv;
import ink.o.w.o.server.io.service.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
public class InitTaskSchedule {
  private final static String MASTER_RANDOM_PASSWORD = UUID.randomUUID().toString();
  private final static String MASTER_NAME = "master";

  private final UserService userService;
  private final MailService mailService;

  @Value("${my.env}")
  private String myEnv;

  @Value("${my.mail}")
  private String myEmail;

  @Value("${spring.profiles.active}")
  private String env;

  public InitTaskSchedule(UserService userService, MailService mailService) {
    this.userService = userService;
    this.mailService = mailService;
  }

  private void initAndCheckMasterUser() {
    logger.info("InitTask: customEnv -> {}, env -> {}", myEnv, env);
    logger.info("InitTask: initPassword -> {}, account -> {}", MASTER_RANDOM_PASSWORD, MASTER_NAME);

    ServiceResult<User> userServiceResult = userService.getUserByUsername(MASTER_NAME);
    if (userServiceResult.getSuccess()) {
      User u = userServiceResult.getPayload();

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      if (encoder.matches(MASTER_RANDOM_PASSWORD, u.getPassword()) || u.getCTime().equals(u.getUTime())) {
        if (env.equals(SystemRuntimeEnv.PRODUCTION)) {
          mailService.sendSystemEmail(
              myEmail,
              "警告：[ 烛火录 ] 请尽快修改管理员密码！",
              String.format("请尽快修改管理员密码！ cTime -> [ %s ]", u.getCTime())
          );
        }
        logger.warn("请尽快修改管理员密码！cTime -> {}", u.getCTime());
        return;
      }

      logger.warn("检测到管理员密码已修改！cTime -> {}, uTime -> {}, isEqual ? {}", u.getCTime(), u.getCTime(), u.getCTime().equals(u.getUTime()));
      return;
    } else {
      userService.register(
          new User()
              .setName(MASTER_NAME)
              .setPassword(MASTER_RANDOM_PASSWORD)
              .setRoles(RoleHelper.toRoles(Roles.MASTER, Roles.USER))
      );
      if (env.equals(SystemRuntimeEnv.PRODUCTION)) {
        mailService.sendSystemEmail(
            myEmail,
            "提醒：[ 烛火录 ] 管理员密码初始化。",
            String.format("密码：[ %s ];", MASTER_RANDOM_PASSWORD)
        );
      }
    }

    System.err.println("执行静态定时任务 [ InitTask ] 时间: " + LocalDateTime.now());

  }

  @Scheduled(fixedRate = 1000 * 60 * 60)
  private void initAndCheckMasterUserTask() {
    initAndCheckMasterUser();
  }
}