package o.w.o.api;

import o.w.o.resource.core.symbols.domain.Symbols;
import o.w.o.resource.core.symbols.service.SymbolsService;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceCreate;
import o.w.o.server.io.api.annotation.APIResourceFetch;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Slf4j
@APIResource(path = "symbols")
@Transactional
public class SymbolsAPI {
  @Resource
  private SymbolsService symbolsService;

  @APIResourceSchema
  public APIResult<APISchemata> fetchSchema() {
    return APIResult.of(APIContext.fetchAPIContext(SymbolsAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceCreate(name = "创建 Symbols")
  public APIResult<?> create(@RequestBody Symbols symbols) {
    var createdInk = symbolsService.create(symbols).guard();
    return APIResult.of(createdInk);
  }

  @APIResourceFetch(path = "/{id}")
  public APIResult<Symbols> fetch(@PathVariable String id) {
    var ink = symbolsService.fetch(id).guard();

    return APIResult.of(ink);
  }
}
