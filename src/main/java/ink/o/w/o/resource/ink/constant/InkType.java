package ink.o.w.o.resource.ink.constant;


import lombok.Getter;

/**
 * InkType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:41
 * @since 1.0.0
 */
public enum InkType {
  /**
   * 文章
   *
   * @date 2020/02/12 16:41
   * @since 1.0.0
   */
  ARTICLE("article");

  /**
   * 类型名称
   *
   * @date 2020/02/12 16:41
   * @since 1.0.0
   */
  @Getter
  private String typeName;

  InkType(String typeName) {
    this.typeName = typeName;
  }
}
