package ink.o.w.o.resource.dot.repository;

import ink.o.w.o.resource.dot.domain.Dot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DotRepository extends JpaRepository<Dot, String> {
}
