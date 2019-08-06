package ink.o.w.o.server.user.service.impl;

import ink.o.w.o.config.exception.ServiceException;
import ink.o.w.o.server.user.domain.UserDO;
import ink.o.w.o.server.user.repository.UserRepository;
import ink.o.w.o.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDO getUserByUsername(String username) {
        UserDO u = userRepository.findUserByName(username);
        return u.getId() == null ? null : u;
    }

    @Override
    public UserDO getUserById(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public UserDO register(UserDO user) {

        final String username = user.getName();

        if (userRepository.findUserByName(username) != null) {
            throw new ServiceException("用户已存在!");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        user.setRoles("USER");

        return userRepository.save(user);
    }

    @Override
    public void unregister(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean changeRole(Integer id, String oldRole, String newRole) {
        return false;
    }

    @Override
    public boolean addRole(Integer id, String role) {
        return false;
    }

    @Override
    public boolean removeRole(Integer id, String role) {
        return false;
    }

    @Override
    public Page<UserDO> listUserByRoles(String roles, Pageable pageable) {
        return userRepository.findUsersByRoles(roles, pageable);
    }

    @Override
    public Page<UserDO> listUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public boolean resetPassword(Integer id) {
        return userRepository.restUserPassword(id) > 0;
    }

    @Override
    public boolean modifyPassword(Integer id, String password, String prevPassword) {
        Optional<UserDO> user = userRepository.findById(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!user.isPresent()) {
            throw new ServiceException("用户不存在！");
        }
        if (!user.get().getPassword().equals(encoder.encode(prevPassword))) {
            throw new ServiceException("旧密码错误！");
        }

        return userRepository.modifySelfPassword(password, id) > 0;
    }

    @Override
    public UserDO modifyProfile(UserDO user, int id) {
        if (userRepository.modifySelfProfile(user.getName(), user.getNickName(), user.getSex(), id) > 0) {
            return userRepository.getOne(id);
        }
        return userRepository.getOne(id);
    }

}
