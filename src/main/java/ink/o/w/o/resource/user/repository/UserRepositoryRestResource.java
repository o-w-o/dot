package ink.o.w.o.resource.user.repository;

import ink.o.w.o.resource.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Role Repository
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:20
 */
@Repository
@RepositoryRestResource(path = "users")
@PreAuthorize("hasRole('MASTER')")
public interface UserRepositoryRestResource extends JpaRepository<User, Integer> {
}
