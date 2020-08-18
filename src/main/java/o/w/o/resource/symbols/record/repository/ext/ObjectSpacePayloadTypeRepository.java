package o.w.o.resource.symbols.record.repository.ext;

import o.w.o.resource.symbols.record.domain.ext.ObjectSpacePayloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectSpacePayloadTypeRepository extends JpaRepository<ObjectSpacePayloadType, Integer> {
}
