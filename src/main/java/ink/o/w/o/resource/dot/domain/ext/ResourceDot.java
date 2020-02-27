package ink.o.w.o.resource.dot.domain.ext;

import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.DotBasic;
import ink.o.w.o.resource.dot.domain.Dotable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@Getter
@Setter
@Entity
@Table(name = "t_dot__resource")
@NoArgsConstructor
public class ResourceDot extends DotBasic implements Dotable<ResourceDot> {

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
  private String folder;

  public ResourceDot(DotBasic dot) {
    hydrate(dot);
  }

  @Override
  public Map<String, Object> dehydrateEx() {
    var source = new HashMap<String, Object>(7);

    source.put("name", this.getName());
    source.put("size", this.getSize());
    source.put("suffix", this.getSuffix());
    source.put("uri", this.getUri());
    source.put("url", this.getUrl());
    source.put("bucket", this.getBucket());
    source.put("folder", this.getFolder());

    return source;
  }

  @Override
  public ResourceDot hydrateEx(DotBasic dotBasic) {
    var source = dotBasic.getSource();
    this.setName((String) source.get("name"));
    this.setSize((Long) source.get("size"));
    this.setSuffix((String) source.get("suffix"));
    this.setUri((String) source.get("uri"));
    this.setUrl((String) source.get("url"));
    this.setBucket((String) source.get("bucket"));
    this.setFolder((String) source.get("folder"));
    return this;
  }
}
