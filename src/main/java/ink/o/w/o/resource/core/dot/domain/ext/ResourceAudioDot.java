package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

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

@JsonTypeName(DotType.TypeName.RESOURCE_AUDIO)

@Entity
@Table(name = "t_dot__resource_audio")
public class ResourceAudioDot extends DotSpace {
  @Override
  public DotType.DotTypeEnum getType() {
    return DotType.DotTypeEnum.RESOURCE_AUDIO;
  }

  /**
   * 文件大小
   */
  private Long size;

  /**
   * 文件的绝对路径
   */
  private String uri;

  /**
   * 文件的web访问地址
   */
  private String url;

  /**
   * 文件后缀
   */
  private String suffix;

  /**
   * 存储的bucket
   */
  private String bucket;

  /**
   * 原文件名
   */
  private String name;
  /**
   * 存储的文件夹
   */
  private String path;
}
