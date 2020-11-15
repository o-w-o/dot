package o.w.o.server.definition;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * EnumEntity
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@MappedSuperclass
public class EnumEntity<T> {
  @Transient
  @Getter(AccessLevel.PROTECTED)
  private T enumValue;

  @Transient
  @Getter(AccessLevel.PROTECTED)
  private boolean enumPreset;

  @Id
  @Getter
  @Setter
  protected Integer id;

  protected static Integer getIntegerUndefinedMark() {
    return -1;
  }

  protected static String getStringUndefinedMark() {
    return "undefined";
  }

  protected static ServiceException getUndefinedException() {
    return ServiceException.of("实体 ID 未设置");
  }

  protected void mountedEnumValue(T enumValue) {
    this.enumPreset = true;
    this.enumValue = enumValue;
  }

  protected boolean throwIfItemNonexistent() {
    return true;
  }
}
