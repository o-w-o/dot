package ink.o.w.o.resource.core.way.repository.ext;

import ink.o.w.o.resource.core.way.domain.ext.SitemapWay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitemapWayRepository extends JpaRepository<SitemapWay, String> {
}
