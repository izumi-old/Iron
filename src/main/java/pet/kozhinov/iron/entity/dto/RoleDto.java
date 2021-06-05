package pet.kozhinov.iron.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private String id;
    private String name;
}
