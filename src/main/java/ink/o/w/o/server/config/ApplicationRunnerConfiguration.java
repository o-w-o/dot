package ink.o.w.o.server.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import ink.o.w.o.resource.system.authorization.repository.AuthorizedJwtStoreRepository;
import ink.o.w.o.resource.system.role.constant.Roles;
import ink.o.w.o.resource.system.role.service.RoleService;
import ink.o.w.o.resource.system.role.util.RoleHelper;
import ink.o.w.o.resource.system.user.constant.UserGender;
import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.resource.system.user.service.UserService;
import ink.o.w.o.server.config.properties.constant.SystemRuntimeEnv;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Component
public class ApplicationRunnerConfiguration implements ApplicationRunner {
  @Resource
  private UserService userService;
  @Resource
  private RoleService roleService;
  @Resource
  private AuthorizedJwtStoreRepository authorizedJwtStoreRepository;
  @Value("${spring.profiles.active}")
  private String env;
  @Resource
  private ObjectMapper objectMapper;

  private void initSystemRoles() {
    logger.info("ApplicationRunner:run 初始化 roles");
    roleService.initRoles();
    logger.info("ApplicationRunner:run 初始化 roles，END");
  }

  private void resetAuthorizedJwtStoreRepository() {
    logger.info("ApplicationRunner:run 清除令牌");
    authorizedJwtStoreRepository.deleteAll();
    logger.info("ApplicationRunner:run 清除令牌，END");
  }

  private void collectAndRegisterJsonTypes() {
    logger.info("ApplicationRunner:run 收集并注册 [JsonTypeName] 类");
    Reflections reflections = new Reflections("ink.o.w.o.resource");
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(JsonTypeName.class);
    classSet.forEach(v -> {
      logger.info("Reflections(ink.o.w.o.resource) clazz -> [{}], registerSubtypes -> [{}]", v.getSimpleName(), v.getAnnotation(JsonTypeName.class).value());
      objectMapper.registerSubtypes(new NamedType(v, v.getAnnotation(JsonTypeName.class).value()));
    });
    logger.info("ApplicationRunner:run 收集并注册 [JsonTypeName] 类，END");
  }

  @Override
  public void run(ApplicationArguments args) {
    logger.info("ApplicationRunner:run ……");

    initSystemRoles();
    resetAuthorizedJwtStoreRepository();
    collectAndRegisterJsonTypes();

    if (!env.contains(SystemRuntimeEnv.PRODUCTION)) {
      userService.register(new User().setName("demo").setRoles(RoleHelper.toRoles(Roles.USER)).setPassword("233333"));
      userService.register(new User().setName("sample").setRoles(RoleHelper.toRoles(Roles.USER, Roles.RESOURCES, Roles.RESOURCES_SAMPLE)).setPassword("233333"));
      userService.register(new User().setName("actuator").setRoles(RoleHelper.toRoles(Roles.USER, Roles.ENDPOINT)).setPassword("233333"));
      userService.register(new User().setName("qa").setRoles(RoleHelper.toRoles(Roles.USER, Roles.MASTER)).setPassword("233333"));
      userService.register(new User().setName("inker").setRoles(RoleHelper.toRoles(Roles.USER)).setPassword("233333"));
      userService.register(new User().setName("李小狼").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.BOY).setPassword("121lxl"));
      userService.register(new User().setName("金闪闪").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.BOY).setPassword("121jss"));
      userService.register(new User().setName("二二娘").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.GIRL).setPassword("121een"));
      userService.register(new User().setName("三三娘").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.GIRL).setPassword("121ssn"));
    }
  }
}
