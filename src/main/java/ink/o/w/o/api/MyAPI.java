package ink.o.w.o.api;

import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import ink.o.w.o.util.ServerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 用户相关 API
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:11
 */
@Slf4j
@RestController
@ExposesResourceFor(MyAPI.class)
@RequestMapping("my")
@PreAuthorize("hasRole('ROLE_USER')")
public class MyAPI {
  private final UserService userService;
  private final ServerHelper serverHelper;

  @Autowired
  public MyAPI(UserService userService, ServerHelper serverHelper) {
    this.userService = userService;
    this.serverHelper = serverHelper;
  }

  @GetMapping({"", "profile"})
  public ResponseEntity<?> getOneUserProfile() {
    var id = serverHelper.getUserIdFormSecurityContext();
    var u = userService.getUserById(id).guard();

    Link link = linkTo(methodOn(UserAPI.class).getOneUserProfile(id)).withSelfRel();

    return ResponseEntityFactory.ok(
        new EntityModel<>(u, link)
    );
  }

  @PatchMapping({"", "profile"})
  public ResponseEntity<?> modifyOneUserProfile(@RequestBody User u) {
    return ResponseEntityFactory.success(
        userService.modifyProfile(u, serverHelper.getUserIdFormSecurityContext())
            .guard()
    );
  }

  @PatchMapping("password")
  public ResponseEntity<?> modifyOneUserPassword(@RequestParam String prevPassword, @RequestParam String password) {
    return ResponseEntityFactory.success(
        userService.modifyPassword(serverHelper.getUserIdFormSecurityContext(), password, prevPassword)
            .guard()
    );
  }
}
