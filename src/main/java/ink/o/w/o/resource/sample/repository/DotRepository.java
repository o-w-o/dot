package ink.o.w.o.resource.sample.repository;

import ink.o.w.o.resource.sample.domain.Dot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotRepository  extends JpaRepository<Dot, Long> {
}
