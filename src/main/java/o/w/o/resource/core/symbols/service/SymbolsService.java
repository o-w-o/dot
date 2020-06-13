package o.w.o.resource.core.symbols.service;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.core.symbols.domain.Symbols;

public interface SymbolsService {
  String test(Symbols symbols);

  ServiceResult<Symbols> create(Symbols symbols);

  ServiceResult<Symbols> fetch(String symbolsId);
}
