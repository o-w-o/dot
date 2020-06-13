package o.w.o.resource.core.symbols.service.handler.ext;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.core.symbols.domain.SymbolsType;
import o.w.o.resource.core.symbols.domain.Symbols;
import o.w.o.resource.core.symbols.domain.SymbolsSpace;

public abstract class AbstractSymbolsHandler {

  abstract public String handle(Symbols symbols);

  abstract public ServiceResult<Symbols> fetch(String inkId, SymbolsType symbolsType);
  abstract public ServiceResult<SymbolsSpace> fetchSpace(String symbolsSpaceId, SymbolsType symbolsType);

  abstract public ServiceResult<Symbols> create(Symbols symbols);

}
