package o.w.o.resource.symbols.record.repository;

import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.repository.FieldSpaceRepository;
import o.w.o.resource.symbols.record.domain.RecordSpace;
import o.w.o.resource.symbols.record.domain.ext.ObjectSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSpaceRepository<T extends RecordSpace> extends JpaRepository<T, String> {
  @Repository
  interface Object extends RecordSpaceRepository<ObjectSpace> {
  }

  @Repository
  interface Resource extends FieldSpaceRepository<ResourceSpace> {
  }
}
