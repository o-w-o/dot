package o.w.o.resource.core.org.repository;

import o.w.o.resource.core.org.domain.OrgType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgTypeRepository extends JpaRepository<OrgType, Integer> {
}
