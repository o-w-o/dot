package o.w.o.resource.system.user.service;

import com.querydsl.core.types.Predicate;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.system.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * @author LongY
 */
public interface UserService {

  ServiceResult<User> getUserByUsername(String username);

  ServiceResult<User> getUserById(Integer id);

  /**
   * 注册新用户,并加密密码
   *
   * @param user User
   * @return user
   */
  ServiceResult<User> register(User user);

  /**
   * 注销用户
   *
   * @param id 用户ID
   */
  ServiceResult<Boolean> revoke(Integer id);

  ServiceResult<User> modifyRoles(Integer id, Set<Role> roles);

  /**
   * 重置用户密码
   *
   * @param id 用户ID
   * @return 状态
   */
  ServiceResult<Boolean> resetPassword(Integer id);

  /**
   * 修改密码
   *
   * @param id           用户ID
   * @param password     新密码
   * @param prevPassword 旧密码
   * @return 状态
   */
  ServiceResult<Boolean> modifyPassword(Integer id, String password, String prevPassword);

  ServiceResult<User> modifyProfile(User user, int id);

  ServiceResult<Page<User>> listUser(Predicate predicate, Pageable pageable);

}
