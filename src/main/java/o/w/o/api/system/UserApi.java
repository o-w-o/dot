package o.w.o.api.system;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.role.domain.Role;
import o.w.o.domain.core.user.domain.User;
import o.w.o.domain.core.user.service.UserService;
import o.w.o.domain.core.user.service.dto.UserProfile;
import o.w.o.domain.core.user.service.dtomapper.UserMapper;
import o.w.o.infrastructure.definition.ApiResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 用户相关 API
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:11
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_MASTER')")
public class UserApi {
  @Resource
  private UserMapper userMapper;

  @Resource
  private UserService userService;

  @ApiOperation("查询用户")
  @GetMapping
  public ApiResult<Page<UserProfile>> listUsers(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
    return ApiResult.of(
        userService.listUser(predicate, pageable).guard().map(u -> userMapper.userToUserProfile(u))
    );
  }

  @ApiOperation("注册用户")
  @PostMapping
  public ApiResult<UserProfile> registerOneUser(@RequestBody User u) {
    return ApiResult.of(
        userMapper.userToUserProfile(userService.register(u).guard())
    );
  }

  @ApiOperation("获取用户信息")
  @GetMapping("{id}")
  public ApiResult<UserProfile> getOneUser(@PathVariable Integer id) {
    var u = userService.getUserById(id)
        .guard();

    return ApiResult.of(userMapper.userToUserProfile(u));
  }

  @ApiOperation("修改用户权限")
  @PostMapping(value = "{id}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResult<UserProfile> modifyOneUserRole(@PathVariable Integer id, @RequestBody Set<Role> roles) {
    return ApiResult.of(
        userMapper.userToUserProfile(
            userService.modifyRoles(id, roles)
                .guard()
        )
    );
  }

  @ApiOperation("重置密码")
  @PostMapping("{id}/password/reset")
  public ApiResult<Boolean> resetOneUserPassword(@PathVariable Integer id) {
    return ApiResult.of(
        userService.resetPassword(id)
            .guard()
    );
  }

  @ApiOperation("注销用户")
  @DeleteMapping("{id}")
  public ApiResult<Boolean> revokeOneUser(@PathVariable Integer id) {
    return ApiResult.of(
        userService.revoke(id)
            .guard()
    );
  }
}
