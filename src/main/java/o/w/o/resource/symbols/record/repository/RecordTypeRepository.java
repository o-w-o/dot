package o.w.o.resource.symbols.record.repository;

import o.w.o.resource.symbols.record.domain.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTypeRepository extends JpaRepository<RecordType, Integer> {
}
