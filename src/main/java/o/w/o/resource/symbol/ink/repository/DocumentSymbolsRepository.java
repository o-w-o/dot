package o.w.o.resource.symbol.ink.repository;

import o.w.o.resource.symbol.ink.domain.ext.DocumentSymbols;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentSymbolsRepository extends JpaRepository<DocumentSymbols, String> {
}
