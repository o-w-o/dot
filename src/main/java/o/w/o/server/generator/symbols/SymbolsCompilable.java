package o.w.o.server.generator.symbols;

import com.squareup.javapoet.JavaFile;

import java.util.List;

public interface SymbolsCompilable {
  List<JavaFile> compile();
}
