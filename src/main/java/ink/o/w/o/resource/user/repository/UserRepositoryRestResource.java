package ink.o.w.o.resource.user.repository;

import ink.o.w.o.resource.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * User
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/10 下午6:43
 */
@Repository
@RepositoryRestResource
@PreAuthorize("hasAnyRole('RESOURCE', 'RESOURCE_EXPLORE')")
public interface UserRepositoryRestResource extends JpaRepository<User, Integer> {
}
