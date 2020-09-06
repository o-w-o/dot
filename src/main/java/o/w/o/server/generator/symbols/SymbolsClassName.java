package o.w.o.server.generator.symbols;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum SymbolsClassName {
  DOMAIN("$", SymbolsPackage.DOMAIN),
  DOMAIN_TYPE("$Type", SymbolsPackage.DOMAIN),
  DOMAIN_TYPE_ENUM("$TypeEnum", SymbolsPackage.DOMAIN),
  DOMAIN_TYPE_NAME("$TypeName", SymbolsPackage.DOMAIN),
  DOMAIN_SPACE("$Space", SymbolsPackage.DOMAIN),
  DOMAIN_SPACE_EXT("@Space", SymbolsPackage.DOMAIN_SPC),
  DOMAIN_SPACE_EXT_PAYLOAD("@SpacePayload", SymbolsPackage.DOMAIN_SPC),
  DOMAIN_SPACE_EXT_PAYLOAD_TYPE("@SpacePayloadType", SymbolsPackage.DOMAIN_SPC),

  REPOSITORY("$Repository", SymbolsPackage.REPOSITORY),
  REPOSITORY_TYPE("$TypeRepository", SymbolsPackage.REPOSITORY),
  REPOSITORY_SPACE("$SpaceRepository", SymbolsPackage.REPOSITORY),
  REPOSITORY_SPACE_EXT("@SpaceRepository", SymbolsPackage.REPOSITORY_SPC_PAYLOAD),
  REPOSITORY_SPACE_EXT_TYPE("@SpacePayloadTypeRepository", SymbolsPackage.REPOSITORY_SPC_PAYLOAD),

  SERVICE("$Service", SymbolsPackage.SERVICE),

  SERVICE_IMPL("$ServiceImpl", SymbolsPackage.SERVICE_IMPL),

  SERVICE_DTO("$DTO", SymbolsPackage.SERVICE_DTO),

  SERVICE_HANDLER("$SpaceHandler", SymbolsPackage.SERVICE_HANDLER),
  SERVICE_HANDLER_FACTORY("$SpaceHandlerFactory", SymbolsPackage.SERVICE_HANDLER),
  SERVICE_HANDLER_HOLDER("$SpaceHandlerHolder", SymbolsPackage.SERVICE_HANDLER),
  SERVICE_HANDLER_TYPE_SELECTOR("$TypeSelector", SymbolsPackage.SERVICE_HANDLER),
  SERVICE_HANDLER_EXT("@$SpaceHandler", SymbolsPackage.SERVICE_HANDLER_EXT),
  SERVICE_HANDLER_EXT_PAYLOAD("@$SpacePayloadHandler", SymbolsPackage.SERVICE_HANDLER_EXT),
  SERVICE_HANDLER_EXT_PAYLOAD_HOLDER("@$SpacePayloadHandlerHolder", SymbolsPackage.SERVICE_HANDLER_EXT),

  UTIL("$Util", SymbolsPackage.UTIL),
  ;

  @Getter
  private final String classNameTpl;
  @Getter
  private final SymbolsPackage packageScope;

  SymbolsClassName(String classNameTpl, SymbolsPackage packageScope) {
    this.classNameTpl = classNameTpl;
    this.packageScope = packageScope;
  }

  public String generateClassName(SymbolsModule.Module module) {
    return StringUtils.replace(this.classNameTpl, SymbolsModule.MODULE_SEPARATOR, module.getClassName());
  }

  public String generateClassName(SymbolsModule.Module module, SymbolsModule.SubModule subModule) {
    return StringUtils.replace(
        StringUtils.replace(this.classNameTpl, SymbolsModule.SUBMODULE_SEPARATOR, subModule.getClassName()),
        SymbolsModule.MODULE_SEPARATOR,
        module.getClassName()
    );
  }
}
