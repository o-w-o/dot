package o.w.o.resource.symbol.dot.repository.ext;

import o.w.o.resource.symbol.dot.domain.ext.ResourceSpacePayloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceDotTypeRepository extends JpaRepository<ResourceSpacePayloadType, Integer> {
}
