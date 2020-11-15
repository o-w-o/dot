package o.w.o.resource.system.user.service.dto;

import lombok.Data;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.user.constant.UserGender;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserProfile {
  private Integer id;
  private String email;

  private String name;
  private String nickName;

  private UserGender gender;

  private Set<Role> roles = new HashSet<>();

}
