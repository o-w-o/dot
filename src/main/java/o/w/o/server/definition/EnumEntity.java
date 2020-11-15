package o.w.o.server.definition;

import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class EnumEntity<T> {
  @Transient
  @Getter(AccessLevel.PROTECTED)
  private T enumValue;

  @Transient
  @Getter(AccessLevel.PROTECTED)
  private boolean enumPreset;

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
