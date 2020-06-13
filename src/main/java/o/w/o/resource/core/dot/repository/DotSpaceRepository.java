package o.w.o.resource.core.dot.repository;

import o.w.o.resource.core.dot.domain.DotSpace;
import o.w.o.resource.core.dot.domain.ext.ResourceSpace;
import o.w.o.resource.core.dot.domain.ext.TextSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotSpaceRepository<T extends DotSpace> extends JpaRepository<T, String> {
  @Repository
  interface Text extends DotSpaceRepository<TextSpace> {
  }

  @Repository
  interface Resource extends DotSpaceRepository<ResourceSpace> {
  }
}
