package o.w.o.resource.integration.aliyun.common.domain.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class Statement {
  @JsonProperty("Effect")
  private Effect effect;
  @JsonProperty("Action")
  private Set<Action> actions;
  @JsonProperty("Resource")
  private Set<Resource> resources;
  @JsonProperty("Condition")
  private Condition condition;
}
