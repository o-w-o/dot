package ink.o.w.o.resource.user.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class UserPassword {
  @Id
  Integer id;

  @NotNull
  String password;
}
