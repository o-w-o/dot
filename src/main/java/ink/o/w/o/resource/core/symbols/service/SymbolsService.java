package ink.o.w.o.resource.core.symbols.service;

import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.server.io.service.ServiceResult;

public interface SymbolsService {
  String test(Symbols symbols);

  ServiceResult<Symbols> create(Symbols symbols);

  ServiceResult<Symbols> fetch(String symbolsId);
}
