package o.w.o.resource.integration.aliyun.common.domain.policy;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Module {
  OSS("oss");

  private String name;

  Module(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }

  public enum OSSOperation {
    GetObject("GetObject"),
    PutObject("PutObject"),
    DeleteObject("DeleteObject"),
    ListParts("ListParts"),
    AbortMultipartUpload("AbortMultipartUpload"),
    ListObjects("ListObjects");

    private String operationName;

    OSSOperation(String operationName) {
      this.operationName = operationName;
    }

    @JsonValue
    public String getOperationName() {
      return operationName;
    }
  }
}
