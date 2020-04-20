package ink.o.w.o.resource.core.symbols.service.handler.ext;

import ink.o.w.o.resource.core.symbols.domain.SymbolsType;
import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.resource.core.symbols.domain.SymbolsSpace;
import ink.o.w.o.server.io.service.ServiceResult;

public abstract class AbstractSymbolsHandler {

  abstract public String handle(Symbols symbols);

  abstract public ServiceResult<Symbols> fetch(String inkId, SymbolsType symbolsType);
  abstract public ServiceResult<SymbolsSpace> fetchSpace(String symbolsSpaceId, SymbolsType symbolsType);

  abstract public ServiceResult<Symbols> create(Symbols symbols);

}
