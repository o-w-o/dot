package o.w.o.resource.example.enumentity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnumEntityExampleRepository extends JpaRepository<EnumEntityExample, Integer> {
}
