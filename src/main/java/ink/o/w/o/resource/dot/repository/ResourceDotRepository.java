package ink.o.w.o.resource.dot.repository;

import ink.o.w.o.resource.dot.domain.ext.ResourceDot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceDotRepository extends JpaRepository<ResourceDot, String> {
}
