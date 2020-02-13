package ink.o.w.o.resource.ink.constant;


import lombok.Getter;

/**
 * InkRefMode 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:39
 * @since 1.0.0
 */
public enum InkRefMode {
  /**
   * 部分
   *
   * @date 2020/02/12 16:39
   * @since 1.0.0
   */
  PART("PART"),
  /**
   * 全部
   *
   * @date 2020/02/12 16:39
   * @since 1.0.0
   */
  ENTIRETY("ENTIRETY"),
  /**
   * 注释
   *
   * @date 2020/02/12 16:39
   * @since 1.0.0
   */
  ANNOTATION("ANNOTATION");

  /**
   * 模式名称
   *
   * @date 2020/02/12 16:39
   * @since 1.0.0
   */
  @Getter
  private String modeName;

  InkRefMode(String modeName) {
    this.modeName = modeName;
  }
}
