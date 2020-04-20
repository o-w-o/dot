package ink.o.w.o.resource.system.authorization.repository;

import ink.o.w.o.resource.system.authorization.domain.AuthorizedJwtStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午7:55
 */

@Repository
public interface AuthorizedJwtStoreRepository extends CrudRepository<AuthorizedJwtStore, String> {
  List<AuthorizedJwtStore> findByUserId(Integer userId);
}
