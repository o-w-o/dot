package ink.o.w.o.resource.ink.repository;

import ink.o.w.o.resource.ink.domain.Ink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InkRepository extends JpaRepository<Ink, String> {
}
