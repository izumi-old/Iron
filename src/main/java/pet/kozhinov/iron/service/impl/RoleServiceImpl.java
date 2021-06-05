package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.repository.RoleRepository;
import pet.kozhinov.iron.service.RoleService;

import java.util.Collection;

@RequiredArgsConstructor
@Service(RoleServiceImpl.NAME)
public class RoleServiceImpl implements RoleService {
    public static final String NAME = "iron_RoleServiceImpl";

    private final RoleRepository repository;

    @Override
    public Collection<Role> getRolesByRoleName(String roleName) {
        return repository.findAllByName(roleName);
    }
}
