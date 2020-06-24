package o.w.o.resource.symbol.ink.repository;

import o.w.o.resource.symbol.ink.domain.Symbols;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsRepository extends JpaRepository<Symbols, String> {
}
