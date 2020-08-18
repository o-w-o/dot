package o.w.o.resource.symbols.field.repository;

import o.w.o.resource.symbols.field.domain.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, String> {
}
