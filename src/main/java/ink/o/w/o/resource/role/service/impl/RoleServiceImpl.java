package ink.o.w.o.resource.role.service.impl;

import ink.o.w.o.resource.role.constant.Roles;
import ink.o.w.o.resource.role.repository.RoleRepository;
import ink.o.w.o.resource.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(new HashSet<>(Roles.ROLES_MAP.values()));
        } else {
            logger.warn("skip [initRoles:{}] step !", roleRepository.count());
        }
    }
}
