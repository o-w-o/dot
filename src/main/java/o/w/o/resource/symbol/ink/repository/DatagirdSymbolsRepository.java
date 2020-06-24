package o.w.o.resource.symbol.ink.repository;

import o.w.o.resource.symbol.ink.domain.ext.DatagirdSymbols;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatagirdSymbolsRepository extends JpaRepository<DatagirdSymbols, String> {
}
