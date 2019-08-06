package ink.o.w.o.server.user.service;

import ink.o.w.o.server.user.domain.UserDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author LongY
 */
public interface UserService {

    public UserDO getUserByUsername(String username);

    public UserDO getUserById(Integer id);

    /**
     * 注册新用户,并加密密码
     *
     * @param user User
     * @return user
     */
    public UserDO register(UserDO user);

    /**
     * 注销用户
     *
     * @param id
     */
    public void unregister(Integer id);

    public boolean changeRole(Integer id, String oldRole, String newRole);

    public boolean addRole(Integer id, String role);

    public boolean removeRole(Integer id, String role);

    /**
     * 重置用户密码
     * @param id 用户ID
     * @return 状态
     */
    public boolean resetPassword(Integer id);
    /**
     * 修改密码
     * @param id 用户ID
     * @param password 新密码
     * @param prevPassword 旧密码
     * @return 状态
     */
    public boolean modifyPassword(Integer id, String password, String prevPassword);

    public UserDO modifyProfile(UserDO user, int id);

    public Page<UserDO> listUserByRoles(String roles, Pageable pageable);

    public Page<UserDO> listUser(Pageable pageable);

}
