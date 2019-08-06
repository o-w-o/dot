package ink.o.w.o.resource.symbols.repository;

import ink.o.w.o.resource.symbols.domain.Sample;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
/**
 * Sample
 * @author symbols@dingtalk.com
 * @version  1.0
 * @date 2019/8/3 下午3:53
 */
@Repository
@RepositoryRestResource
@PreAuthorize("hasRole('ROLE_SAMPLE')")
public interface SampleRepository extends ElasticsearchRepository<Sample, String> {
}
