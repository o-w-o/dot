package o.w.o.resource.core.dot.repository.ext;

import o.w.o.resource.core.dot.domain.ext.TextSpacePayloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextDotTypeRepository extends JpaRepository<TextSpacePayloadType, Integer> {
}