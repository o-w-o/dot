package ink.o.w.o.resource.user.service.impl;

import com.querydsl.core.types.Predicate;
import ink.o.w.o.resource.user.constant.UserConstant;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.repository.UserRepository;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.util.PasswordEncoderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ServiceResult<User> getUserByUsername(String username) {
        if (userRepository.existsByName(username)) {
            return ServiceResultFactory.success(
                userRepository.findUserByName(username)
                    .orElseThrow(new ServiceException("用户不存在！"))
            );
        }

        return ServiceResultFactory.error("用户不存在！");

    }

    @Override
    public ServiceResult<User> getUserById(Integer id) {
        if (userRepository.existsById(id)) {
            return ServiceResultFactory.success(
                userRepository.findById(id)
                    .orElseThrow(
                        new ServiceException(String.format("[ %d ] 用户不存在", id))
                    )
            );
        }

        return ServiceResultFactory.error(String.format("[ %d ] 用户不存在", id));
    }

    @Override
    public ServiceResult<User> register(User user) {
        if (userRepository.existsByName(user.getName())) {
            return ServiceResultFactory.error(String.format("[ %d ] 用户已存在!", user.getId()));
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        return ServiceResultFactory.success(
            userRepository.save(user)
        );
    }

    @Override
    public ServiceResult<Boolean> unregister(Integer id) {
        userRepository.deleteById(id);
        return ServiceResultFactory.success(
            userRepository.findById(id)
                .isEmpty()
        );
    }

    @Override
    public ServiceResult<Boolean> changeRole(Integer id, String oldRole, String newRole) {
        return ServiceResultFactory.success(false);
    }

    @Override
    public ServiceResult<Page<User>> listUser(Predicate predicate, Pageable pageable) {
        return ServiceResultFactory.success(
            userRepository.findAll(predicate, pageable)
        );
    }

    @Override
    public ServiceResult<Boolean> resetPassword(Integer id) {
        return ServiceResultFactory.success(
            userRepository.modifyUserPassword(PasswordEncoderHelper.encoder().encode(UserConstant.USER_INITIAL_PASSWORD), id) > 0
        );
    }

    @Override
    public ServiceResult<Boolean> modifyPassword(Integer id, String password, String prevPassword) throws ServiceException {
        logger.info("prevPassword -> {}, password -> {}", prevPassword, password);

        if (password.equals(prevPassword)) {
            throw new ServiceException("新旧密码相同！");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userRepository.findById(id).ifPresentOrElse(
            (u) -> {
                logger.info("prevPassword -> {}, password -> {}", u.getPassword(), encoder.matches(u.getPassword(), prevPassword));
                if (!encoder.matches(u.getPassword(), prevPassword)) {
                    throw new ServiceException("旧密码错误！");
                }
            },
            () -> {
                throw new ServiceException("用户不存在！");
            }
        );

        return ServiceResultFactory.success(
            userRepository.modifyUserPassword(encoder.encode(password), id) > 0
        );
    }

    @Override
    public ServiceResult<User> modifyProfile(User user, int id) {
        if (userRepository.existsByNameAndIdIsNot(user.getName(), id)) {
            return ServiceResultFactory.error("用户名已存在！");
        }

        if (userRepository.modifyUserProfile(user.getName(), user.getNickName(), user.getSex(), id) > 0) {
            return ServiceResultFactory.success(
                userRepository.getOne(id)
            );
        }

        return ServiceResultFactory.error("更新用户信息失败！");
    }

}
