package pet.kozhinov.iron.service;

import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.Role;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Validated
public interface RoleService {
    Collection<Role> getRolesByRoleName(@NotBlank String roleName);
}
