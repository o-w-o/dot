package ink.o.w.o.resource.core.symbols.domain;


import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

/**
 * SymbolsType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:41
 * @since 1.0.0
 */
@Data

@Entity
@Table(name = "t_symbols_type")
public class SymbolsType {

  @Id
  private String id;

  @Enumerated(value = EnumType.STRING)
  private SymbolsTypeEnum type;

  public enum SymbolsTypeEnum {
    /**
     * 文章
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DOCUMENT(TypeName.DOCUMENT),

    /**
     * 伴生文章
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DERIVANT(TypeName.DERIVANT),

    /**
     * 模板
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    TEMPLATE(TypeName.TEMPLATE),

    /**
     * 数据网格
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DATAGIRD(TypeName.DATAGIRD)
    ;

    /**
     * 类型名称
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    @Getter
    private final String typeName;

    SymbolsTypeEnum(String typeName) {
      this.typeName = typeName;
    }
  }

  public static class TypeName {
    public static final String DOCUMENT = "DOCUMENT";
    public static final String DERIVANT = "DERIVANT";
    public static final String DATAGIRD = "DATAGIRD";
    public static final String TEMPLATE = "TEMPLATE";
  }
}
