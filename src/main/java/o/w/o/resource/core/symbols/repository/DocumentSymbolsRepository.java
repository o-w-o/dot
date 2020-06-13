package o.w.o.resource.core.symbols.repository;

import o.w.o.resource.core.symbols.domain.ext.DocumentSymbols;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentSymbolsRepository extends JpaRepository<DocumentSymbols, String> {
}
