package o.w.o.resource.core.symbols.repository;

import o.w.o.resource.core.symbols.domain.Symbols;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsRepository extends JpaRepository<Symbols, String> {
}
