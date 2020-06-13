package o.w.o.resource.core.dot.repository;

import o.w.o.resource.core.dot.domain.DotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotTypeRepository extends JpaRepository<DotType, Integer> {
}
