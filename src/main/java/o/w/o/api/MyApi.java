package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.resource.system.user.service.dto.UserProfile;
import o.w.o.resource.system.user.service.dtomapper.UserMapper;
import o.w.o.server.definition.ApiResult;
import o.w.o.server.util.ServiceUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户相关 API
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5
 */
@Slf4j
@RestController
@RequestMapping("my")
@PreAuthorize("hasRole('ROLE_USER')")
public class MyApi {
  @Resource
  UserMapper userMapper;

  @Resource
  private UserService userService;

  @GetMapping("profile")
  public ApiResult<UserProfile> getOneUserProfile() {
    var id = ServiceUtil.getPrincipalUserId();
    var u = userService.getUserById(id).guard();

    return ApiResult.of(userMapper.userToUserProfile(u));
  }

  @PatchMapping("profile")
  public ApiResult<UserProfile> modifyOneUserProfile(@RequestBody User u) {
    return ApiResult.of(
        userMapper.userToUserProfile(
            userService.modifyProfile(u, ServiceUtil.getPrincipalUserId())
                .guard()
        )
    );
  }

  @PatchMapping(path = "password")
  public ApiResult<Boolean> modifyOneUserPassword(
      @RequestParam String prevPassword,
      @RequestParam String password,
      @AuthenticationPrincipal AuthorizedUser authorizedUser
  ) {
    return ApiResult.of(
        userService.modifyPassword(authorizedUser.getId(), password, prevPassword)
            .guard()
    );
  }
}
