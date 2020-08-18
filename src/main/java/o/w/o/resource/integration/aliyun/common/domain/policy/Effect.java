package o.w.o.resource.integration.aliyun.common.domain.policy;

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
