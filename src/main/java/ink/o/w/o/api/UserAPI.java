package ink.o.w.o.api;

import com.querydsl.core.types.Predicate;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.AuthorizedUser;
import ink.o.w.o.server.domain.HttpResponseData;
import ink.o.w.o.server.domain.HttpResponseDataFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(HttpConstant.API_ENTRY + "/users")
public class UserAPI {
    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public HttpResponseData listUsers(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        return HttpResponseDataFactory.success(
            userService.listUser(predicate, pageable)
                .guard()
        );
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public HttpResponseData register(@RequestBody User u) {
        return HttpResponseDataFactory.success(
            userService.register(u)
                .guard()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MASTER') or (hasRole('ROLE_USER') and principal.username.equals(#id.toString()))")
    public HttpResponseData getOneUserProfile(@PathVariable Integer id) {
        return HttpResponseDataFactory.success(
            userService.getUserById(id)
                .guard()
        );
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MASTER') or (hasRole('ROLE_USER') and principal.username.equals(#id.toString()))")
    public HttpResponseData modifyOneUserProfile(@PathVariable Integer id, @RequestBody User u) {
        return HttpResponseDataFactory.success(
            userService.modifyProfile(u, id)
                .guard()
        );
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ROLE_USER') and principal.username.equals(#id.toString())")
    public HttpResponseData modifyOneUserPassword(@PathVariable Integer id, String prevPassword, String password) {
        return HttpResponseDataFactory.success(
            userService.modifyPassword(id, password, prevPassword)
                .guard()
        );
    }

    @PatchMapping("/{id}/password/reset")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponseData resetOneUserPassword(@PathVariable Integer id) {
        return HttpResponseDataFactory.success(
            userService.resetPassword(id)
                .guard()
        );
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponseData modifyOneUserRoles(@PathVariable Integer id, String roles) {
        AuthorizedUser authorizedUser = (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return HttpResponseDataFactory.success(
            userService.changeRole(id, authorizedUser.getRoles(), roles)
                .guard()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public HttpResponseData revokeOneUser(@PathVariable Integer id) {
        return HttpResponseDataFactory.success(
            userService.unregister(id)
                .guard()
        );
    }
}
