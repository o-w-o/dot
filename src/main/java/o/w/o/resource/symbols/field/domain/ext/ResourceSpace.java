package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.domain.FieldType;
import o.w.o.resource.system.user.domain.User;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * 资源空间
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonEntityProperty
@JsonTypeName(FieldType.TypeName.RESOURCE)

@Entity
@Table(name = "t_s_field__resource")
public class ResourceSpace extends FieldSpace<ResourceSpacePayload, ResourceSpacePayloadType> {
  /**
   * 原文件名
   */
  @NotBlank
  private String name;

  /**
   * 文件后缀
   */
  private String suffix;

  /**
   * 文件大小
   */
  @NotNull
  @Min(1)
  private Long size;

  /**
   * 文件的标识
   */
  @NotBlank
  private String urn;

  /**
   * 文件的 web 访问地址
   */
  @NotBlank
  @URL(protocol = "https", host = "o-w-o.store")
  private String url;

  /**
   * 存储的文件夹
   */
  @NotBlank
  private String dir;

  @NotNull
  @Enumerated
  private Stage stage;

  @NotNull
  @Enumerated
  private Visibility visibility;

  @ManyToOne
  private User owner;

  /**
   * 存储库
   */
  private String storageRepository;

  @Transient
  private File file;

  @Override
  public FieldType.TypeEnum getType() {
    return FieldType.TypeEnum.RESOURCE;
  }

  public enum Stage {
    STORED,
    STAGING,
    STAGED,
    PERSISTING,
    PERSISTED,
    REMOVED,
    DESTROYED,
  }

  public enum Visibility {
    /**
     * 无
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    NONE("NONE"),

    /**
     * 公开
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    PUBLIC("public"),

    /**
     * 私有
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    PRIVATE("private"),

    /**
     * 保护
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    PROTECT("protect"),

    /**
     * 中转站
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    TRANSIT ("temporary"),

    /**
     * 垃圾箱
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    BIN("bin");

    /**
     * 类型名称
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    @Getter
    private final String name;

    Visibility(String name) {
      this.name = name;
    }
  }
}
