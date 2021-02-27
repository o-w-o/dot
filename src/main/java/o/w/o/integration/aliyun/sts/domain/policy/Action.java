package o.w.o.integration.aliyun.sts.domain.policy;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public abstract class Action {
  @Getter
  @Setter
  String moduleActionName;

  @Getter
  @Setter
  String module;

  @JsonValue
  public String toJson() {
    return String.format("%s:%s", this.getModule(), this.getModuleActionName());
  }
}
