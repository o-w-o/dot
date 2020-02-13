package ink.o.w.o.resource.ink.constant;


import lombok.Getter;

/**
 * InkUnitType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:42
 * @since 1.0.0
 */
public enum InkUnitType {

  /**
   * 附件
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  ATTACHMENT("ATTACHMENT"),
  /**
   * 图像
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  IMAGE("IMAGE"),
  /**
   * 句子
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  SENTENCE("SENTENCE");

  /**
   * 类型名称
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  @Getter
  private String typeName;

  InkUnitType(String typeName) {
    this.typeName = typeName;
  }
}
