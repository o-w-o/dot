package o.w.o.server.schedule;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.notification.domain.MailTemplate;
import o.w.o.resource.system.notification.service.MailService;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.role.util.RoleUtil;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.server.configuration.properties.constant.SystemRuntimeEnv;
import o.w.o.server.definition.ServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SystemPassword 任务
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/11/28 11:21
 */

@Slf4j
@Component
public class SystemPasswordTaskSchedule {
  private static final String MASTER_NAME = "master";
  private static String MASTER_RANDOM_PASSWORD = UUID.randomUUID().toString();

  @Resource
  private PasswordEncoder passwordEncoder;
  @Resource
  private UserService userService;
  @Resource
  private MailService mailService;

  @Value("${my.mail}")
  private String postmasterEmail;

  @Value("${spring.profiles.active}")
  private String env;

  public User registerMaster() {
    return this.userService
        .register(
            new User()
                .setEmail(postmasterEmail)
                .setName(MASTER_NAME)
                .setPassword(MASTER_RANDOM_PASSWORD)
                .setRoles(RoleUtil.of(Role.MASTER, Role.USER))
        )
        .guard();
  }

  public User resetMasterRandomPassword(User u) {
    MASTER_RANDOM_PASSWORD = UUID.randomUUID().toString();

    return this.userService
        .register(u.setPassword(MASTER_RANDOM_PASSWORD))
        .guard();
  }

  public void notifyMasterPasswordUnsafe(User u) {
    if (this.env.equals(SystemRuntimeEnv.PRODUCTION)) {
      this.mailService.send(
          MailTemplate
              .builder()
              .receiver(u.getEmail())
              .subject("警告：[ 烛火录 ] 请尽快修改管理员密码！")
              .body(String.format("请尽快修改管理员密码！ cTime -> [ %s ]", u.getCTime()))
              .build()
      );
    }
  }

  public void notifyMasterInitialized(User u) {
    if (this.env.equals(SystemRuntimeEnv.PRODUCTION)) {
      this.mailService.send(
          MailTemplate
              .builder()
              .receiver(u.getEmail())
              .subject("提醒：[ 烛火录 ] 管理员密码初始化。")
              .body(String.format("密码：[ %s ];", MASTER_RANDOM_PASSWORD))
              .build()
      );

      logger.warn("请尽快修改管理员密码！cTime -> {}", u.getCTime());
    }
  }


  private void initAndCheckMasterUser() {
    logger.info("SystemPasswordTask: env -> {}", this.env);

    ServiceResult<User> userServiceResult = this.userService.getUserByUsername(MASTER_NAME);
    if (userServiceResult.isSuccess()) {
      User u = userServiceResult.getPayload();

      if (passwordEncoder.matches(MASTER_RANDOM_PASSWORD, u.getPassword())) {
        if (u.getCTime().equals(u.getUTime())) {
          resetMasterRandomPassword(u);
          logger.warn("SystemPasswordTask: resetPassword -> {}, account -> {}", MASTER_RANDOM_PASSWORD, MASTER_NAME);
        }
        notifyMasterPasswordUnsafe(u);
      }
    } else {
      notifyMasterInitialized(registerMaster());
      logger.info("SystemPasswordTask: initPassword -> {}, account -> {}", MASTER_RANDOM_PASSWORD, MASTER_NAME);
    }

    logger.info("执行静态定时任务 [ SystemPasswordTask ] 时间: {}", LocalDateTime.now());
  }

  @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 1000 * 10)
  private void initAndCheckMasterUserTask() {
    this.initAndCheckMasterUser();
  }
}
