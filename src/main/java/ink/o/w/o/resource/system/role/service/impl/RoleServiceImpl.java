package ink.o.w.o.resource.system.role.service.impl;

import ink.o.w.o.resource.system.role.repository.RoleRepository;
import ink.o.w.o.resource.system.role.service.RoleService;
import ink.o.w.o.resource.system.role.util.RoleHelper;
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
    private RoleRepository roleRepository;

    @Override
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(new HashSet<>(RoleHelper.ROLES_MAP.values()));
        } else {
            logger.warn("skip [initRoles:{}] step !", roleRepository.count());
        }
    }
}
