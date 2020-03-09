package ink.o.w.o.integration.aliyun.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import ink.o.w.o.integration.aliyun.domain.policy.Statement;
import lombok.Data;

import java.util.Set;

@Data
public class Policy {
  @JsonProperty("Version")
  private String version = "1";

  @JsonProperty("Statement")
  private Set<Statement> statements;
}
