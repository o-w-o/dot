package ink.o.w.o.resource.system.role.repository;

import ink.o.w.o.resource.system.role.domain.Role;
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
