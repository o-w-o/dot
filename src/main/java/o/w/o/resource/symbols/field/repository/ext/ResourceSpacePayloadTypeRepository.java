package o.w.o.resource.symbols.field.repository.ext;

import o.w.o.resource.symbols.field.domain.ext.ResourceSpacePayloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceSpacePayloadTypeRepository extends JpaRepository<ResourceSpacePayloadType, Integer> {
}
