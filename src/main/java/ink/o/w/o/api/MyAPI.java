package ink.o.w.o.api;

import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.resource.system.user.service.UserService;
import ink.o.w.o.server.io.api.APISchemata;
import ink.o.w.o.server.io.api.annotation.*;
import ink.o.w.o.server.io.api.APIException;
import ink.o.w.o.server.io.api.APIResult;
import ink.o.w.o.util.ContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户相关 API
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:11
 */
@Slf4j
@APIResource(path = "my")
@PreAuthorize("hasRole('ROLE_USER')")
public class MyAPI {
  private final UserService userService;

  @Autowired
  public MyAPI(UserService userService) {
    this.userService = userService;
  }

  @APIResourceSchema
  public APIResult<APISchemata> fetchSchema() {
    return APIResult.of(ContextHelper.fetchAPIContext(MyAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceFetch(path = {"", "profile"})
  public APIResult<?> getOneUserProfile() {
    var id = ContextHelper.getUserIdFormSecurityContext();
    var u = userService.getUserById(id).guard();

    return APIResult.of(u);
  }

  @APIResourceModify(path = {"", "profile"})
  public APIResult<?> modifyOneUserProfile(@RequestBody User u) {
    return APIResult.of(
        userService.modifyProfile(u, ContextHelper.getUserIdFormSecurityContext())
            .guard()
    );
  }

  @APIResourceModify(path = "password")
  public APIResult<?> modifyOneUserPassword(@RequestParam String prevPassword, @RequestParam String password) {
    return APIResult.of(
        userService.modifyPassword(ContextHelper.getUserIdFormSecurityContext(), password, prevPassword)
            .guard()
    );
  }
}
