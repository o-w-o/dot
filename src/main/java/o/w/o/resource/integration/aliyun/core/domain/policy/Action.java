package o.w.o.resource.integration.aliyun.core.domain.policy;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Action {
  OSS_GetObject(Module.OSS, Module.OSSOperation.GetObject.getOperationName()),
  OSS_PutObject(Module.OSS, Module.OSSOperation.PutObject.getOperationName()),
  OSS_DeleteObject(Module.OSS, Module.OSSOperation.DeleteObject.getOperationName()),
  OSS_ListObjects(Module.OSS, Module.OSSOperation.ListObjects.getOperationName());

  @Getter
  private String moduleActionName;

  @Getter
  private Module module;

  Action(Module module, String moduleActionName) {
    this.module = module;
    this.moduleActionName = moduleActionName;
  }

  @JsonValue
  public String toJson() {
    return String.format("%s:%s", this.getModule().getName(), this.getModuleActionName());
  }
}
