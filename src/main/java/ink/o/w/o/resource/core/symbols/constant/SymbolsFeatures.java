package ink.o.w.o.resource.core.symbols.constant;


import lombok.Getter;

/**
 * InkFeatureType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:40
 * @since 1.0.0
 */
public enum SymbolsFeatures {
  /**
   * 无
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  NONE("NONE");

  /**
   * 类型名称
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  @Getter
  private String typeName;

  SymbolsFeatures(String typeName) {
    this.typeName = typeName;
  }
}
