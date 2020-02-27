package ink.o.w.o.resource.dot.constant;


import lombok.Getter;

/**
 * DotType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:42
 * @since 1.0.0
 */
public enum DotType {

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
  RESOURCE("RESOURCE"),
  /**
   * 句子
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  SENTENCE("SENTENCE"),
  /**
   * 通用
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  JSON("JSON");

  /**
   * 类型名称
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  @Getter
  private String typeName;

  DotType(String typeName) {
    this.typeName = typeName;
  }
}
