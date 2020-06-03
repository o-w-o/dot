package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpacePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 资源单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.RESOURCE)
@JsonEntityProperty
@JsonTypedSpace

@Entity
@Table(name = "t_dot__resource")
public class ResourceSpace extends DotSpace {
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  protected Object payloadContent;
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
  private String path;
  /**
   * 存储的 bucket
   */
  private String bucket;

  @Valid
  @Transient
  @JsonTypedSpacePayload
  private ResourceSpacePayload payload;
  @ManyToOne
  private ResourceSpacePayloadType payloadType;

  @Override
  public DotType.TypeEnum getType() {
    return DotType.TypeEnum.RESOURCE;
  }
}
