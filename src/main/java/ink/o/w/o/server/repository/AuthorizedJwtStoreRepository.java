package ink.o.w.o.server.repository;

import ink.o.w.o.server.domain.AuthorizedJwtStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午7:55
 */

@Repository
public interface AuthorizedJwtStoreRepository extends CrudRepository<AuthorizedJwtStore, String> {
}
