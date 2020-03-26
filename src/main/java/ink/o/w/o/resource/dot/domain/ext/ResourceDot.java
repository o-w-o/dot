package ink.o.w.o.resource.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.DotSpace;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

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

@JsonTypeName(DotType.TypeName.RESOURCE)

@Entity
@Table(name = "t_dot__resource")
public class ResourceDot extends DotSpace {

  private final DotType type = DotType.RESOURCE;

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
