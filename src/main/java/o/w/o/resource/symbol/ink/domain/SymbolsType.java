package o.w.o.resource.symbol.ink.domain;


import o.w.o.resource.symbol.ink.repository.SymbolsTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * SymbolsType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:41
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_symbols_type")
public class SymbolsType {

  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private SymbolsTypeEnum type;

  public SymbolsType(SymbolsTypeEnum symbolsTypeEnum) {
    this.id = symbolsTypeEnum.id;
    this.type = symbolsTypeEnum;
  }

  @EntityEnumerated(enumClass = SymbolsTypeEnum.class, entityClass = SymbolsType.class, repositoryClass = SymbolsTypeRepository.class)
  public enum SymbolsTypeEnum {
    /**
     * 文章
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DOCUMENT(1, TypeName.DOCUMENT),

    /**
     * 伴生文章
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DERIVANT(2, TypeName.DERIVANT),

    /**
     * 模板
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    TEMPLATE(9, TypeName.TEMPLATE),

    /**
     * 数据网格
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DATAGIRD(0, TypeName.DATAGIRD);

    /**
     * 类型名称
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    @Getter
    private final String typeName;
    @Getter
    private final Integer id;

    SymbolsTypeEnum(Integer id, String typeName) {
      this.typeName = typeName;
      this.id = id;
    }
  }

  public static class TypeName {
    public static final String DOCUMENT = "DOCUMENT";
    public static final String DERIVANT = "DERIVANT";
    public static final String DATAGIRD = "DATAGIRD";
    public static final String TEMPLATE = "TEMPLATE";
  }
}
