package ink.o.w.o.api;

import com.querydsl.core.types.Predicate;
import ink.o.w.o.resource.system.role.domain.Role;
import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.resource.system.user.service.UserService;
import ink.o.w.o.server.io.api.APIContext;
import ink.o.w.o.server.io.api.APIException;
import ink.o.w.o.server.io.api.APIResult;
import ink.o.w.o.server.io.api.APISchemata;
import ink.o.w.o.server.io.api.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

/**
 * 用户相关 API
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:11
 */
@Slf4j
@APIResource(path = "users")
@PreAuthorize("hasRole('ROLE_MASTER')")
public class UserAPI {

  private final UserService userService;

  @Autowired
  public UserAPI(UserService userService) {
    this.userService = userService;
  }

  @APIResourceSchema
  public APIResult<APISchemata> fetchSchema() {
    return APIResult.of(APIContext.fetchAPIContext(UserAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceFetch
  public APIResult<?> listUsers(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
    return APIResult.of(
        userService.listUser(predicate, pageable)
            .guard()
    );
  }

  @APIResourceCreate(name = "注册用户")
  public APIResult<?> registerOneUser(@RequestBody User u) {
    return APIResult.of(
        userService.register(u)
            .guard()
    );
  }

  @APIResourceFetch(path = "{id}")
  @PreAuthorize("hasRole('ROLE_MASTER') or (hasRole('ROLE_USER') and principal.username.equals(#id.toString()))")
  public APIResult<?> getOneUserProfile(@PathVariable Integer id) {
    var u = userService.getUserById(id)
        .guard();

    return APIResult.of(u);
  }

  @APIResourceCreate(path = "{id}/roles", name = "权限修改")
  @PreAuthorize("hasRole('ROLE_MASTER')")
  public APIResult<?> modifyOneUserRole(@PathVariable Integer id, @RequestBody Set<Role> roles) {
    return APIResult.of(
        userService.modifyRoles(id, roles)
            .guard()
    );
  }

  @APIResourceCreate(path = "{id}/password/reset", name = "密码重置")
  public APIResult<?> resetOneUserPassword(@PathVariable Integer id) {
    return APIResult.of(
        userService.resetPassword(id)
            .guard()
    );
  }

  @APIResourceDestroy(path = "{id}", name = "注销用户")
  public APIResult<?> revokeOneUser(@PathVariable Integer id) {
    return APIResult.of(
        userService.unregister(id)
            .guard()
    );
  }
}
