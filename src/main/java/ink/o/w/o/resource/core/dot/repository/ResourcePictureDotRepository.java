package ink.o.w.o.resource.core.dot.repository;

import ink.o.w.o.resource.core.dot.domain.ext.ResourcePictureDot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcePictureDotRepository extends JpaRepository<ResourcePictureDot, String> {
}
