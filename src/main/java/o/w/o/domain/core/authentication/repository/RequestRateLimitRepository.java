package o.w.o.domain.core.authentication.repository;

import o.w.o.domain.core.authentication.domain.RequestRateLimit;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

/**
 * RequestRateLimitRepository
 *
 * @author symbols@dingtalk.com
 * @date 2019/11/19
 */

@Repository
public interface RequestRateLimitRepository extends KeyValueRepository<RequestRateLimit, Long> {
}
