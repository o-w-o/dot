package o.w.o.server.generator.symbols;

import com.squareup.javapoet.JavaFile;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.generator.symbols.def.Record;
import o.w.o.server.io.system.SystemContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SymbolsGenerator {
  public static Map<SymbolsMaterial, JavaFile> generateFiledModule() {
    return generateFiledModule(true);
  }

  public static Map<SymbolsMaterial, JavaFile> generateFiledModule(boolean skipPrecious) {
    var list = new HashMap<SymbolsMaterial, JavaFile>();
    var record = Record.create().materialize()
        .stream()
        .filter(v -> !skipPrecious || !v.isPrecious())
        .collect(Collectors.toMap(v -> v, SymbolsMaterial::compile));

    list.putAll(record);
    return list;
  }

  public static String generateJavadoc(String title) {
    return StringUtils.joinWith(
        "\n",
        title,
        "",
        "@author symbols@dingtalk.com",
        StringUtils.join("@date ", DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd"))
    );
  }

  public static void main(String[] args) {
    logger.info("[SymbolsGenerator] START {}", SystemContext.PROJECT_GENERATE_CODE_PATH);
    generateFiledModule(false).forEach((m, f) -> {
      try {
        if(m.isPrecious()) {
          f.writeTo(Path.of(SystemContext.PROJECT_SOURCE_CODE_PATH));
        } else {
          f.writeTo(Path.of(SystemContext.PROJECT_GENERATE_CODE_PATH));
        }
      } catch (IOException e) {
        logger.error("异常！", e);
      }
    });
    logger.info("[SymbolsGenerator] END");
  }
}
