package o.w.o.resource.symbols.field.repository;

import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.domain.ext.ElementSpace;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldSpaceRepository<T extends FieldSpace> extends JpaRepository<T, String> {
  @Repository
  interface Text extends FieldSpaceRepository<ElementSpace> {
  }

  @Repository
  interface Resource extends FieldSpaceRepository<ResourceSpace> {
  }
}
