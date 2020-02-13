package ink.o.w.o.server.config;

import ink.o.w.o.resource.role.constant.Roles;
import ink.o.w.o.resource.role.service.RoleService;
import ink.o.w.o.resource.role.util.RoleHelper;
import ink.o.w.o.resource.user.constant.UserGender;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.config.properties.constant.SystemRuntimeEnv;
import ink.o.w.o.server.repository.AuthorizedJwtStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ApplicationRunnerConfiguration implements ApplicationRunner {
    @Resource
    UserService userService;
    @Resource
    RoleService roleService;
    @Resource
    AuthorizedJwtStoreRepository authorizedJwtStoreRepository;
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("ApplicationRunner:run ……");

        logger.info("ApplicationRunner:run 初始化 roles");
        roleService.initRoles();
        logger.info("ApplicationRunner:run 初始化 roles，END");

        logger.info("ApplicationRunner:run 清除令牌");
        authorizedJwtStoreRepository.deleteAll();
        logger.info("ApplicationRunner:run 清除令牌，END");

        if (SystemRuntimeEnv.DEVELOPMENT.equals(env)) {
            userService.register(new User().setName("demo").setRoles(RoleHelper.toRoles(Roles.USER)).setPassword("233333"));
            userService.register(new User().setName("sample").setRoles(RoleHelper.toRoles(Roles.USER, Roles.RESOURCES, Roles.RESOURCES_SAMPLE)).setPassword("233333"));
            userService.register(new User().setName("actuator").setRoles(RoleHelper.toRoles(Roles.USER, Roles.ENDPOINT)).setPassword("233333"));
            userService.register(new User().setName("qa").setRoles(RoleHelper.toRoles(Roles.USER, Roles.MASTER)).setPassword("233333"));
            userService.register(new User().setName("李小狼").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.BOY).setPassword("121lxl"));
            userService.register(new User().setName("金闪闪").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.BOY).setPassword("121jss"));
            userService.register(new User().setName("二二娘").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.GIRL).setPassword("121een"));
            userService.register(new User().setName("三三娘").setRoles(RoleHelper.toRoles(Roles.USER)).setGender(UserGender.GIRL).setPassword("121ssn"));

        }
    }
}