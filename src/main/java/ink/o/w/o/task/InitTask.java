package ink.o.w.o.task;

import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.domain.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@Configuration      // 1. 主要用于标记配置类，兼备 Component 的效果。
@EnableScheduling   // 2. 开启定时任务
public class InitTask {
    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    UserService userService;

    private final static String masterRandomPassword = UUID.randomUUID().toString();
    private final static String masterName = "master";
    private final static String masterRoleName = "MASTER:USER";

    // 3. 添加定时任务 corn 或 其他
    @Scheduled(fixedRate = 1000 * 60 * 60)
    private void initAndCheckMasterUser() {
        logger.info("InitTask: initPassword -> {}, account -> {}", masterRandomPassword, masterName);

        ServiceResult<User> userServiceResult = userService.getUserByUsername(masterName);
        if (userServiceResult.getSuccess()) {
            User u = userServiceResult.getPayload();

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(masterRandomPassword, u.getPassword()) || u.getCTime().equals(u.getUTime())) {
                logger.warn("请尽快修改管理员密码！cTime -> {}", u.getCTime());
                return;
            }

            logger.warn("检测到管理员密码已修改！cTime -> {}, uTime -> {}, isEqual ? {}", u.getCTime(), u.getCTime(), u.getCTime().equals(u.getUTime()));
            return;
        }

        userService.register(
            new User()
                .setName(masterName)
                .setPassword(masterRandomPassword)
                .setRoles(masterRoleName)
        );
        System.err.println("执行静态定时任务 [ InitTask ] 时间: " + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 7)
    private void initUsersForDevelopmentEnv() {
        if(env.equals("development")) {
            userService.register(new User().setName("李小狼").setRoles("USER").setPassword("121lxl"));
            userService.register(new User().setName("金闪闪").setRoles("USER").setPassword("121jss"));
            userService.register(new User().setName("二二娘").setRoles("USER").setPassword("121een"));
            userService.register(new User().setName("三三娘").setRoles("USER").setPassword("121ssn"));
        }
    }
}
