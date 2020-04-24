package ink.o.w.o.resource.core.dot.domain.ext;

import ink.o.w.o.resource.core.dot.domain.DotSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 音频单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@MappedSuperclass
public abstract class Resource extends DotSpace {

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
}
