package o.w.o.resource.symbol.ink.service.handler.ext;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.ink.domain.SymbolsType;
import o.w.o.resource.symbol.ink.domain.Symbols;
import o.w.o.resource.symbol.ink.domain.SymbolsSpace;

public abstract class AbstractSymbolsHandler {

  abstract public String handle(Symbols symbols);

  abstract public ServiceResult<Symbols> fetch(String inkId, SymbolsType symbolsType);
  abstract public ServiceResult<SymbolsSpace> fetchSpace(String symbolsSpaceId, SymbolsType symbolsType);

  abstract public ServiceResult<Symbols> create(Symbols symbols);

}
