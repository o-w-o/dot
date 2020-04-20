package ink.o.w.o.resource.core.symbols.repository;

import ink.o.w.o.resource.core.symbols.domain.Symbols;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsRepository extends JpaRepository<Symbols, String> {
}
