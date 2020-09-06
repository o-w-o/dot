package o.w.o.server.generator.symbols;

import lombok.Getter;
import o.w.o.server.io.system.SystemContext;
import org.apache.commons.lang3.StringUtils;

public enum SymbolsPackage {
  DOMAIN("domain"),
  DOMAIN_SPC("domain.space"),
  DOMAIN_SPC_PAYLOAD("domain.space.payload"),
  REPOSITORY("repository"),
  REPOSITORY_SPC_PAYLOAD("repository.space.payload"),
  SERVICE("service"),
  SERVICE_IMPL("service.impl"),
  SERVICE_DTO("service.dto"),
  SERVICE_HANDLER("service.handler"),
  SERVICE_HANDLER_EXT("service.handler.ext"),
  UTIL("util"),

  ;

  @Getter
  private final String namespace;

  SymbolsPackage(String namespace) {
    this.namespace = namespace;
  }

  public String generatePackageName(SymbolsModule.Module module) {
    return StringUtils.joinWith(
        SystemContext.PKG_SEPARATOR,
        SymbolsModule.SYMBOLS_PKG_NAME,
        module.getNamespace(),
        this.namespace
    );
  }
}
