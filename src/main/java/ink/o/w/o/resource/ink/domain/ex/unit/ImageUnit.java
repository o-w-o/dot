package ink.o.w.o.resource.ink.domain.ex.unit;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 图片单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:48
 * @since 1.0.0
 */
@Entity
@Table(name = "t_ink_unit__image")
public class ImageUnit extends AbstractInkUnit {
  /**
   * 图片 url
   *
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  String url;
  /**
   * 大小
   *
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  String size;
}
