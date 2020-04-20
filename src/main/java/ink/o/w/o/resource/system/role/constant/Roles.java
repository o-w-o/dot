package ink.o.w.o.resource.system.role.constant;

import ink.o.w.o.resource.system.role.domain.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Roles {
  public final static Role ANONYMOUS = new Role().setId(0).setName("ANONYMOUS");
  public final static Role MASTER = new Role().setId(1).setName("MASTER");
  public final static Role USER = new Role().setId(9).setName("USER");
  public final static Role ENDPOINT = new Role().setId(10).setName("ENDPOINT");
  public final static Role RESOURCES = new Role().setId(11).setName("RESOURCES");
  public final static Role RESOURCES_USER = new Role().setId(111).setName("RESOURCES:USER");
  public final static Role RESOURCES_ROLE = new Role().setId(112).setName("RESOURCES:ROLE");
  public final static Role RESOURCES_SAMPLE = new Role().setId(113).setName("RESOURCES:SAMPLE");
}
