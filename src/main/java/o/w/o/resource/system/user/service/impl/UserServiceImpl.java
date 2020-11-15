package o.w.o.resource.system.user.service.impl;

import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.user.constant.UserConstant;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.repository.UserRepository;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.server.definition.ServiceException;
import o.w.o.server.definition.ServiceResult;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@CacheConfig(cacheNames = "srv")
@Service
public class UserServiceImpl implements UserService {

  @Resource
  private UserRepository userRepository;

  @Resource
  private PasswordEncoder passwordEncoder;

  @Cacheable(key = "'U:fb_uname#' + #username", condition = "#result?.isSuccess()", sync = true)
  @Override
  public ServiceResult<User> getUserByUsername(String username) {
    var notFoundMessage = String.format("用户 [%s] 不存在！", username);

    if (userRepository.existsByName(username)) {
      return ServiceResult.success(
          userRepository.findUserByName(username)
              .orElseThrow(new ServiceException(notFoundMessage))
      );
    }

    return ServiceResult.error(notFoundMessage);
  }

  @Cacheable(key = "'U:fb_id#' + #id", condition = "#result?.isSuccess()", sync = true)
  @Override
  public ServiceResult<User> getUserById(Integer id) {
    return ServiceResult.success(
        userRepository.findById(id)
            .orElseThrow(
                new ServiceException(String.format("[ %d ] 用户不存在", id))
            )
    );
  }

  @Override
  public ServiceResult<User> register(User user) {
    if (userRepository.existsByName(user.getName())) {
      return ServiceResult.error(String.format("[ %s ] 用户已存在!", user.getName()));
    }

    if (user.getPassword() == null) {
      throw new ServiceException("密码不得为空！");
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setPassword(encoder.encode(user.getPassword()));

    return ServiceResult.success(
        userRepository.save(user)
    );
  }

  @CacheEvict(key = "'U:fb_id#' + #id", condition = "#result?.isSuccess()")
  @Override
  public ServiceResult<Boolean> revoke(Integer id) {
    var u = getUserById(id);
    if (u.isSuccess()) {
      userRepository.deleteById(id);

      return ServiceResult.success(
          userRepository.findById(id)
              .isEmpty()
      );
    } else {
      throw ServiceException.of(u.getMessage());
    }
  }

  @CacheEvict(key = "'U:fb_id#' + #id", condition = "#result?.isSuccess()")
  @Override
  public ServiceResult<User> modifyRoles(Integer id, Set<Role> roles) {
    if (roles.size() == 0) {
      throw new ServiceException("请至少分配 [ 1个 ] 权限！");
    }
    roles.forEach(role -> {
      if (role.getName().equals(Role.Enum.MASTER.getRoleName())) {
        throw new ServiceException("请勿分配 MASTER 权限！");
      }
    });

    var u = getUserById(id).guard();

    u.getRoles().forEach(role -> {
      if (role.getName().equals(Role.Enum.MASTER.getRoleName())) {
        throw new ServiceException("请勿变动 MASTER 权限！");
      }
    });

    return ServiceResult.success(
        userRepository.save(u.setRoles(roles))
    );
  }

  @Override
  public ServiceResult<Page<User>> listUser(Predicate predicate, Pageable pageable) {
    return ServiceResult.success(
        userRepository.findAll(predicate, pageable)
    );
  }

  @Override
  public ServiceResult<Boolean> resetPassword(Integer id) {
    return ServiceResult.success(
        userRepository.modifyUserPassword(passwordEncoder.encode(UserConstant.USER_INITIAL_PASSWORD), id) > 0
    );
  }

  @Override
  public ServiceResult<Boolean> modifyPassword(Integer id, String password, String prevPassword) throws ServiceException {
    if (password.equals(prevPassword)) {
      throw new ServiceException("新旧密码相同！");
    }

    var u = getUserById(id).guard();

    logger.debug("modifyPassword: [RUN] match ? {}", passwordEncoder.matches(prevPassword, u.getPassword()));
    if (passwordEncoder.matches(prevPassword, u.getPassword())) {
      userRepository.save(u.setPassword(passwordEncoder.encode(password)));
    } else {
      throw new ServiceException("旧密码错误！");
    }

    return ServiceResult.success(true);
  }

  @CacheEvict(key = "'U:fb_id#' + #id", condition = "#result?.isSuccess()")
  @Override
  public ServiceResult<User> modifyProfile(User user, int id) {
    if (userRepository.existsByNameAndIdIsNot(user.getName(), id)) {
      return ServiceResult.error("用户名已存在！");
    }

    User persistUser = getUserById(id).guard();

    if (user.getName() != null) {
      persistUser.setName(user.getName());
    }

    if (user.getNickName() != null) {
      persistUser.setNickName(user.getNickName());
    }

    if (user.getGender() != null) {
      persistUser.setGender(user.getGender());
    }

    return ServiceResult.success(
        userRepository.save(persistUser)
    );
  }

}
