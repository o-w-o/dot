package o.w.o.resource.system.authorization.repository;

import o.w.o.resource.system.authorization.domain.AuthorizationStub;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AuthorizationStubRepository
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午7:55
 */

@Repository
public interface AuthorizationStubRepository extends KeyValueRepository<AuthorizationStub, String> {

  /**
   * findByUserId
   *
   * @param uid 用户 ID
   * @return List<AuthorizationStub> {@link AuthorizationStub}
   * @author symbols@dingtalk.com
   * @date 2020/11/15
   */
  List<AuthorizationStub> findByUid(Integer uid);
}
