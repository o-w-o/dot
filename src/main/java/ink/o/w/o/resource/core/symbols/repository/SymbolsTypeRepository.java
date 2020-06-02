package ink.o.w.o.resource.core.symbols.repository;

import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.resource.core.symbols.domain.SymbolsType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsTypeRepository extends JpaRepository<SymbolsType, String> {
}
