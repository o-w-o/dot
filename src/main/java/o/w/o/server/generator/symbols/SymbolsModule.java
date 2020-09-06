package o.w.o.server.generator.symbols;

import lombok.*;
import o.w.o.server.io.system.SystemContext;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SymbolsModule {
  public static final String MODULE_SEPARATOR = "$";
  public static final String SUBMODULE_SEPARATOR = "@";
  public static final String SYMBOLS_PKG_NAME = StringUtils.joinWith(SystemContext.PKG_SEPARATOR, SystemContext.PKG_ENTRY, "resource", "symbols");

  protected Module module;
  protected SymbolsMaterial entity;
  protected SymbolsMaterial entityType;
  protected SymbolsMaterial entityTypeName;
  protected SymbolsMaterial entityTypeEnum;
  protected SymbolsMaterial entitySpace;
  @Singular("entitySpace")
  protected List<SymbolsMaterial> entitySpaces;
  protected SymbolsMaterial entitySpaceType;
  protected SymbolsMaterial entitySpacePayload;
  @Singular("entitySpacePayload")
  protected List<SymbolsMaterial> entitySpacePayloads;

  protected SymbolsMaterial repository;
  protected SymbolsMaterial repositoryType;
  protected SymbolsMaterial repositorySpace;

  protected SymbolsMaterial service;
  protected SymbolsMaterial serviceImpl;

  protected SymbolsMaterial serviceDto;

  public SymbolsModule(Module module) {
    this.module = module;
  }

  public static SymbolsModule of(Module module) {
    return new SymbolsModule(module);
  }

  public enum Module {
    FIELD("field"),
    RECORD("record"),
    RELATION("relation"),
    WAY("way"),
    ;

    @Getter
    private final String namespace;

    Module(String namespace) {
      this.namespace = namespace;
    }

    public String getClassName() {
      return StringUtils.capitalize(this.namespace);
    }
  }

  public enum SubModule {
    FIELD_ELEMENT(Module.FIELD, "element", 1),
    FIELD_RESOURCE(Module.FIELD, "resource", 2),
    FIELD_SCHEMA(Module.FIELD, "schema", 3),

    RECORD_OBJECT(Module.RECORD, "object", 1),
    RECORD_SUBJECT(Module.RECORD, "subject", 2),
    RECORD_DIALECTIC(Module.RECORD, "dialectic", 3),

    RELATION_SET(Module.RELATION, "set", 1),
    RELATION_TREE(Module.RELATION, "tree", 2),
    RELATION_GRAPH(Module.RELATION, "graph", 3),

    WAY_DEFAULT(Module.WAY, "default", 0),
    ;

    @Getter
    private final Module module;
    @Getter
    private final String namespace;
    @Getter
    private final Integer id;

    SubModule(Module module, String namespace, Integer id) {
      this.module = module;
      this.namespace = namespace;
      this.id = id;
    }

    public String getClassName() {
      return StringUtils.capitalize(this.namespace);
    }

    public String getEnumName() {
      return StringUtils.upperCase(this.namespace);
    }
  }
}
