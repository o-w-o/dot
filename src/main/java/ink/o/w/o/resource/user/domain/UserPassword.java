package ink.o.w.o.resource.user.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class UserPassword {
  @Id
  private Integer id;

  @NotNull
  private String password;
}
