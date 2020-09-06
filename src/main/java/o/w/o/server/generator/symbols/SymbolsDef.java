package o.w.o.server.generator.symbols;

import com.squareup.javapoet.JavaFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import o.w.o.server.generator.symbols.def.common.EntityDef;
import o.w.o.server.generator.symbols.def.common.RepositoryDef;
import o.w.o.server.generator.symbols.def.common.ServiceDef;
import o.w.o.server.generator.symbols.def.common.ServiceHandlerDef;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public abstract class SymbolsDef implements SymbolsCompilable {
  private SymbolsModule module;
  private List<SymbolsMaterial> materials;

  public SymbolsDef(SymbolsModule.Module module) {
    this.module = SymbolsModule.of(module);
    this.materials = new ArrayList<>();
  }

  public List<SymbolsMaterial> materialize() {
    this.process();
    var list = new ArrayList<SymbolsMaterial>();

    list.add(module.getEntity());
    list.add(module.getEntityType());
    list.add(module.getEntityTypeName());
    list.add(module.getEntityTypeEnum());
    list.add(module.getEntitySpace());

    list.add(module.getRepository());
    list.add(module.getRepositoryType());
    list.add(module.getRepositorySpace());

    list.add(module.getService());
    list.add(module.getServiceImpl());
    list.add(module.getServiceDto());

    return list;
  }

  public void beforeProcess() {
  }

  public void process() {
    beforeProcess();

    processEntity();
    processRepository();
    processService();
    processServiceHandler();

    afterProcess();
  }

  public void afterProcess() {
  }

  public void processEntity() {
    var m = module.getModule();

    module
        .setEntity(EntityDef.generateEntity(m))
        .setEntityType(EntityDef.generateEntityType(m))
        .setEntityTypeName(EntityDef.generateEntityTypeName(m))
        .setEntityTypeEnum(EntityDef.generateEntityTypeEnum(m))
        .setEntitySpace(EntityDef.generateEntitySpace(m))
    ;
  }

  public void processRepository() {
    var m = module.getModule();
    module
        .setRepository(RepositoryDef.generateEntityRepository(m))
        .setRepositoryType(RepositoryDef.generateEntityTypeRepository(m))
        .setRepositorySpace(RepositoryDef.generateEntitySpaceRepository(m))
    ;
  }

  public void processService() {
    var m = module.getModule();
    module
        .setService(ServiceDef.generateService(m))
        .setServiceImpl(ServiceDef.generateServiceImpl(m))
    ;
  }

  public void processServiceHandler() {
    var m = module.getModule();
    module
        .setServiceDto(ServiceHandlerDef.generateServiceDto(m))
    ;
  }

  @Override
  public List<JavaFile> compile() {
    return this
        .materialize()
        .stream()
        .map(SymbolsMaterial::compile)
        .collect(Collectors.toList());
  }
}
