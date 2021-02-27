package o.w.o.integration.aliyun.sts.domain.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class Statement {
  @JsonProperty("Effect")
  private Effect effect;
  @JsonProperty("Action")
  private Set<? extends Action> actions;
  @JsonProperty("Resource")
  private Set<String> resources;
  @JsonProperty("Condition")
  private Condition condition;
}
