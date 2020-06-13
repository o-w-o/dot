package o.w.o.resource.core.symbols.repository;

import o.w.o.resource.core.symbols.domain.ext.DatagirdSymbols;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatagirdSymbolsRepository extends JpaRepository<DatagirdSymbols, String> {
}
