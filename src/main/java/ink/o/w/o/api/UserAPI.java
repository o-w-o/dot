package ink.o.w.o.api;

import com.querydsl.core.types.Predicate;
import ink.o.w.o.resource.role.domain.Role;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@ExposesResourceFor(UserAPI.class)
@RequestMapping("users")
@PreAuthorize("hasRole('ROLE_MASTER')")
public class UserAPI {

  private final EntityLinks entityLinks;
  private final UserService userService;

  @Autowired
  public UserAPI(EntityLinks entityLinks, UserService userService) {
    this.entityLinks = entityLinks;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<?> listUsers(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
    return ResponseEntityFactory.ok(
        userService.listUser(predicate, pageable)
            .guard()
    );
  }

  @PostMapping
  public ResponseEntity<?> registerOneUser(@RequestBody User u) {
    return ResponseEntityFactory.ok(
        userService.register(u)
            .guard()
    );
  }

  @GetMapping("{id}")
  @PreAuthorize("hasRole('ROLE_MASTER') or (hasRole('ROLE_USER') and principal.username.equals(#id.toString()))")
  public ResponseEntity<?> getOneUserProfile(@PathVariable Integer id) {
    var u = userService.getUserById(id)
        .guard();
    Link link = entityLinks
        .linkFor(UserAPI.class, id).withSelfRel();
    return ResponseEntityFactory.ok(
        new EntityModel<>(u, link)
    );
  }

  @PostMapping("{id}/roles")
  @PreAuthorize("hasRole('ROLE_MASTER')")
  public ResponseEntity<?> modifyOneUserRole(@PathVariable Integer id, @RequestBody Set<Role> roles) {
    return ResponseEntityFactory.success(
        userService.modifyRoles(id, roles)
            .guard()
    );
  }

  @PostMapping("{id}/password/reset")
  public ResponseEntity<?> resetOneUserPassword(@PathVariable Integer id) {
    return ResponseEntityFactory.success(
        userService.resetPassword(id)
            .guard()
    );
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> revokeOneUser(@PathVariable Integer id) {
    return ResponseEntityFactory.success(
        userService.unregister(id)
            .guard()
    );
  }
}
