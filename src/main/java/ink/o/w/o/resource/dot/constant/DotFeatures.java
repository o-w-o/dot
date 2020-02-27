package ink.o.w.o.resource.dot.constant;


import lombok.Getter;

/**
 * DotFeatures 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:40
 * @since 1.0.0
 */
public enum DotFeatures {
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

  DotFeatures(String typeName) {
    this.typeName = typeName;
  }
}
