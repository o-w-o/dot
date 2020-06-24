package o.w.o.resource.symbol.way.repository.ext;

import o.w.o.resource.symbol.way.domain.ext.SitemapWay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitemapWayRepository extends JpaRepository<SitemapWay, String> {
}
