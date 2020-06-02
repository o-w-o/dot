package ink.o.w.o.resource.core.dot.repository;

import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.ext.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotSpaceRepository<T extends DotSpace> extends JpaRepository<T, String> {
  @Repository
  interface Text extends DotSpaceRepository<TextDot> {
  }

  @Repository
  interface Resource extends DotSpaceRepository<ResourceSpace> {
  }

  @Repository
  interface LinkPicture extends DotSpaceRepository<LinkPictureDot> {
  }

  @Repository
  interface LinkEmbed extends DotSpaceRepository<LinkEmbedDot> {
  }
}
