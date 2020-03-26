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
  ATTACHMENT(TypeName.ATTACHMENT),
  /**
   * 图像
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  IMAGE(TypeName.IMAGE),
  RESOURCE(TypeName.RESOURCE),
  /**
   * 句子
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  SENTENCE(TypeName.SENTENCE),
  /**
   * 通用
   *
   * @date 2020/02/12 16:42
   * @since 1.0.0
   */
  JSON(TypeName.JSON);

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

  public static class TypeName {
      public static final String ATTACHMENT = "ATTACHMENT";
      public static final String IMAGE = "IMAGE";
      public static final String RESOURCE = "RESOURCE";
      public static final String SENTENCE = "SENTENCE";
      public static final String JSON = "JSON";
  }
}
