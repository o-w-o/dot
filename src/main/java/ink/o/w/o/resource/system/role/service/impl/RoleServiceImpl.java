package ink.o.w.o.resource.system.role.service.impl;

import ink.o.w.o.resource.system.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
  @Override
  public void initRoles() {
  }
}
