package ink.o.w.o.resource.core.dot.repository;

import ink.o.w.o.resource.core.dot.domain.Dot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DotRepository extends JpaRepository<Dot, String> {
}
