package pet.kozhinov.iron.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PersonDto {
    private String id;
    private String email;
    private String phoneNumber;

    @JsonIgnoreProperties(allowSetters = true)
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;

    public String getUsername() {
        return email != null ? email : phoneNumber;
    }
}
