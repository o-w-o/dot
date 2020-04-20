package ink.o.w.o.resource.core.symbols.repository;

import ink.o.w.o.resource.core.symbols.domain.ext.DocumentSymbols;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentSymbolsRepository extends JpaRepository<DocumentSymbols, String> {
}
