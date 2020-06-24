package o.w.o.resource.symbol.dot.repository;

import o.w.o.resource.symbol.dot.domain.Dot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotRepository extends JpaRepository<Dot, String> {
}
