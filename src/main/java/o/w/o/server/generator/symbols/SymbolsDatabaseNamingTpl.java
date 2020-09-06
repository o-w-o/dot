package o.w.o.server.generator.symbols;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SymbolsDatabaseNamingTpl {
  DOMAIN("t_sym_$"),
  DOMAIN_TYPE("t_sym_$_type"),
  DOMAIN_SPACE_EXT("t_sym_$__@"),
  DOMAIN_SPACE_EXT_TYPE("t_sym_$__@_type"),
  ;
  @Getter
  final String tpl;

  SymbolsDatabaseNamingTpl(String tpl) {
    this.tpl = tpl;
  }

  public static String generateTableNameByTpl(SymbolsDatabaseNamingTpl tpl, SymbolsModule.Module module, SymbolsModule.SubModule subModule) {
    return switch (tpl) {
      case DOMAIN_SPACE_EXT, DOMAIN_SPACE_EXT_TYPE -> Stream
          .of(tpl.getTpl())
          .map(t -> StringUtils.replace(t, SymbolsModule.MODULE_SEPARATOR, module.getNamespace()))
          .map(t -> StringUtils.replace(t, SymbolsModule.SUBMODULE_SEPARATOR, subModule.getNamespace()))
          .collect(Collectors.joining());
      default -> throw new IllegalStateException("Unexpected value: " + tpl);
    };
  }

  public static String generateTableNameByTpl(SymbolsDatabaseNamingTpl tpl, SymbolsModule.Module module) {
    return switch (tpl) {
      case DOMAIN, DOMAIN_TYPE -> StringUtils
          .replace(tpl.getTpl(), SymbolsModule.MODULE_SEPARATOR, module.getNamespace());
      default -> throw new IllegalStateException("Unexpected value: " + tpl);
    };
  }
}
