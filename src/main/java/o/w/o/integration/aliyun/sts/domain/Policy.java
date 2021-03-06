package o.w.o.integration.aliyun.sts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import o.w.o.integration.aliyun.sts.domain.policy.Statement;
import o.w.o.infrastructure.definition.SystemException;

import java.util.Set;

/**
 * 阿里云 OSS Policy
 *
 * @author symbols@dingtalk.com
 * @date 2020/2/27
 */
@Data
public class Policy implements Cloneable {
  @JsonProperty("Version")
  private String version = "1";

  @JsonProperty("Statement")
  private Set<Statement> statements;

  @Override
  public Policy clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      throw SystemException.of(SystemException.ExceptionEnum.CLONE);
    }
    return new Policy().setStatements(Set.copyOf(statements));
  }
}
