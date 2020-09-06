package o.w.o.server.generator.symbols.def;

import o.w.o.server.generator.symbols.SymbolsDef;
import o.w.o.server.generator.symbols.SymbolsModule;

public class Field extends SymbolsDef {

  private Field() {
    super(SymbolsModule.Module.FIELD);
  }

  public static Field of() {
    return new Field();
  }
}
