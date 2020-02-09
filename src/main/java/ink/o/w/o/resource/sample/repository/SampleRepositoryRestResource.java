package ink.o.w.o.resource.sample.repository;

import ink.o.w.o.resource.sample.domain.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Sample
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/3 下午3:53
 */
@Repository
@RepositoryRestResource
@PreAuthorize("(hasRole('RESOURCES:SAMPLE') and hasRole('RESOURCES')) or hasRole('MASTER')")
public interface SampleRepositoryRestResource extends JpaRepository<Sample, String> {
}
