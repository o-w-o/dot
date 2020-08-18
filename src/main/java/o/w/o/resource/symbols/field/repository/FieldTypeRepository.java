package o.w.o.resource.symbols.field.repository;

import o.w.o.resource.symbols.field.domain.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTypeRepository extends JpaRepository<FieldType, Integer> {
}
