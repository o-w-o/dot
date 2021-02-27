package o.w.o.integration.aliyun.oss.domain.sts.policy;

import com.fasterxml.jackson.annotation.JsonValue;
import o.w.o.integration.aliyun.sts.domain.policy.Action;

public class OssAction extends Action {

  public static final OssAction OSS_GetObject = new OssAction(Module.OSS, Module.Operation.GetObject);
  public static final OssAction OSS_PutObject = new OssAction(Module.OSS, Module.Operation.PutObject);
  public static final OssAction OSS_DeleteObject = new OssAction(Module.OSS, Module.Operation.DeleteObject);
  public static final OssAction OSS_ListObjects = new OssAction(Module.OSS, Module.Operation.ListObjects);


  public OssAction(Module module, Module.Operation operation) {
    this.setModule(module.getName());
    this.setModuleActionName(operation.getOperationName());
  }

  public enum Module {
    OSS("oss");

    private final String name;

    Module(String name) {
      this.name = name;
    }

    @JsonValue
    public String getName() {
      return name;
    }

    public enum Operation {
      GetObject("GetObject"),
      PutObject("PutObject"),
      DeleteObject("DeleteObject"),
      ListParts("ListParts"),
      AbortMultipartUpload("AbortMultipartUpload"),
      ListObjects("ListObjects");

      private final String operationName;

      Operation(String operationName) {
        this.operationName = operationName;
      }

      @JsonValue
      public String getOperationName() {
        return operationName;
      }
    }
  }
}
