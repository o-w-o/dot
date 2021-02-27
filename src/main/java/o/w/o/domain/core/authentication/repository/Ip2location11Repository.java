package o.w.o.domain.core.authentication.repository;

import o.w.o.domain.core.authentication.domain.Ip2location11;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Ip2location11Repository extends JpaRepository<Ip2location11, Long>, JpaSpecificationExecutor<Ip2location11> {
  @Query(value = "from Ip2location11 l where l.ipFrom <= ?1 and l.ipTo >= ?1")
  List<Ip2location11> locate(Long ip);
}