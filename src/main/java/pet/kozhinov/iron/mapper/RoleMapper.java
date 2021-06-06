package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.entity.dto.RoleDto;
import pet.kozhinov.iron.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class RoleMapper implements Mapper<Role, RoleDto> {
    public static final String NAME = "iron_RoleMapper";

    @Override
    public RoleDto map1(Role from) {
        RoleDto dto = new RoleDto();
        dto.setId(from.getId().toString());
        dto.setName(from.getName().replace("ROLE_", ""));
        return dto;
    }

    @Override
    public Role map2(RoleDto to) {
        throw new ForbiddenException();
    }
}
