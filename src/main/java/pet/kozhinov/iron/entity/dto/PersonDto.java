package pet.kozhinov.iron.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PersonDto {

    @NotBlank
    @JsonIgnoreProperties(allowSetters = true)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String id;
    private String email;
    private String phoneNumber;
    private String patronymic;
    private String passportSeriesAndNumber;

    @JsonIgnoreProperties(allowGetters = true)
    private Collection<RoleDto> roles;

    @NotBlank
    public String getUsername() {
        return email != null ? email : phoneNumber;
    }
}
