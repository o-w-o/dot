package o.w.o.resource.core.symbols.repository;

import o.w.o.resource.core.symbols.domain.SymbolsType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsTypeRepository extends JpaRepository<SymbolsType, String> {
}
