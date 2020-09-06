package o.w.o.server.generator.symbols.def;

import o.w.o.server.generator.symbols.SymbolsDef;
import o.w.o.server.generator.symbols.SymbolsModule;

public class Record extends SymbolsDef {

  private Record() {
    super(SymbolsModule.Module.RECORD);
  }

  public static Record create() {
    return new Record();
  }
}
