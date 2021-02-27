package o.w.o.domain.core.role.repository;

import o.w.o.domain.core.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Role Repository
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:20
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
