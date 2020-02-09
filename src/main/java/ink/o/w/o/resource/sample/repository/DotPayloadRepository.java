package ink.o.w.o.resource.sample.repository;

import ink.o.w.o.resource.sample.domain.DotPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotPayloadRepository extends JpaRepository<DotPayload, Long> {
}
