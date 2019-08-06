package ink.o.w.o.resource.user.repository;

import ink.o.w.o.resource.user.constant.UserConstant;
import ink.o.w.o.resource.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author symbols@dingtalk.com
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findUserByName(String name);

    public Page<User> findUsersByRoles(String roles, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u." +
            "name=?1,u.nickName=?2,u.sex=?3 where u.id=?4")
    int modifySelfProfile(String name, String nickName, Integer sex, Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.password=?1 where u.id=?2")
    int modifySelfPassword(String password, Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.password=" + UserConstant.USER_INITIAL_PASSWORD + " where u.id=?1")
    int restUserPassword(Integer id);

}
