package o.w.o.resource.core.way.repository;

import o.w.o.resource.core.way.domain.Way;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayRepository extends JpaRepository<Way, String> {
}
