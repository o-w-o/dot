package o.w.o.resource.symbol.dot.repository;

import o.w.o.resource.symbol.dot.domain.DotSpace;
import o.w.o.resource.symbol.dot.domain.ext.ResourceSpace;
import o.w.o.resource.symbol.dot.domain.ext.TextSpace;
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
