package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 图片单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.RESOURCE_PICTURE)

@Entity
@Table(name = "t_dot__resource_picture")
public class ResourcePictureDot extends DotSpace {
  /**
   * 文件大小
   */
  @NotNull
  @Min(1)
  private Long size;
  /**
   * 文件的绝对路径
   */
  @NotBlank
  private String uri;
  /**
   * 文件的web访问地址
   */
  @NotBlank
  @URL
  private String url;
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
   * 存储的bucket
   */
  private String bucket;
  /**
   * 存储的文件夹
   */
  @NotBlank
  private String path;

  @Override
  public DotType.DotTypeEnum getType() {
    return DotType.DotTypeEnum.RESOURCE_PICTURE;
  }
}
