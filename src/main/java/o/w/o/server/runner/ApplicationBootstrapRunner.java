package o.w.o.server.runner;

import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.role.util.RoleHelper;
import o.w.o.server.config.properties.constant.SystemRuntimeEnv;
import o.w.o.resource.system.authorization.repository.AuthorizedJwtStoreRepository;
import o.w.o.resource.system.user.constant.UserGender;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.server.config.OrderConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Order(OrderConfiguration.DATASOURCE_AFTER)
public class ApplicationBootstrapRunner implements ApplicationRunner {
  @Resource
  private UserService userService;
  @Resource
  private AuthorizedJwtStoreRepository authorizedJwtStoreRepository;
  @Value("${spring.profiles.active}")
  private String env;

  private void resetAuthorizedJwtStoreRepository() {
    logger.info("ApplicationRunner: [RUN] 清除令牌");
    authorizedJwtStoreRepository.deleteAll();
    logger.info("ApplicationRunner: [RUN] 清除令牌，END");
  }


  @Override
  public void run(ApplicationArguments args) {
    logger.info("ApplicationRunner: [START]");

    resetAuthorizedJwtStoreRepository();

    if (!env.contains(SystemRuntimeEnv.PRODUCTION)) {
      userService.register(new User().setName("demo").setRoles(RoleHelper.toRoles(Role.USER)).setPassword("233333"));
      userService.register(new User().setName("sample").setRoles(RoleHelper.toRoles(Role.USER, Role.RESOURCES, Role.RESOURCES_SAMPLE)).setPassword("233333"));
      userService.register(new User().setName("actuator").setRoles(RoleHelper.toRoles(Role.USER, Role.ENDPOINT)).setPassword("233333"));
      userService.register(new User().setName("qa").setRoles(RoleHelper.toRoles(Role.USER, Role.MASTER)).setPassword("233333"));
      userService.register(new User().setName("inker").setRoles(RoleHelper.toRoles(Role.USER)).setPassword("233333"));
      userService.register(new User().setName("李小狼").setRoles(RoleHelper.toRoles(Role.USER)).setGender(UserGender.BOY).setPassword("121lxl"));
      userService.register(new User().setName("金闪闪").setRoles(RoleHelper.toRoles(Role.USER)).setGender(UserGender.BOY).setPassword("121jss"));
      userService.register(new User().setName("二二娘").setRoles(RoleHelper.toRoles(Role.USER)).setGender(UserGender.GIRL).setPassword("121een"));
      userService.register(new User().setName("三三娘").setRoles(RoleHelper.toRoles(Role.USER)).setGender(UserGender.GIRL).setPassword("121ssn"));
    }

    logger.info("ApplicationRunner: [END]");
  }
}
