package o.w.o.resource.symbol.ink.constant;


import lombok.Getter;

/**
 * InkParticipantsVisibility 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:40
 * @since 1.0.0
 */
public enum SymbolsParticipantsVisibility {
  /**
   * 无
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  NONE("NONE"),

  /**
   * 无
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  PUBLIC("PUBLIC"),

  /**
   * 黑名单
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  BLACKLIST("BLACKLIST"),

  /**
   * 白名单
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  WHITELIST("WHITELIST");

  /**
   * 类型名称
   *
   * @date 2020/02/12 16:40
   * @since 1.0.0
   */
  @Getter
  private String visibility;

  SymbolsParticipantsVisibility(String visibility) {
    this.visibility = visibility;
  }
}
