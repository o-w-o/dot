package ink.o.w.o.resource.aliyun.domain.policy;

import lombok.Getter;

public enum Effect {
  Allow("Allow"),
  Deny("Deny");

  @Getter
  private String value;

  Effect(String value) {
    this.value = value;
  }
}
