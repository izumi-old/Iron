package pet.kozhinov.iron.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.dto.PersonDto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public interface PersonService {
    PersonDto signup(@Valid PersonDto personDto);
    Optional<PersonDto> getPersonById(@NotBlank String id);
    Optional<Person> getPersonByLogin(@NotBlank String login);
    Optional<Person> getPersonByEmail(@NotBlank @Email String email);
    Optional<Person> getPersonByPhoneNumber(@NotBlank String phoneNumber);

    Page<PersonDto> getPersons(@NotNull Pageable pageable);
    Page<PersonDto> getPersonsByRole(@NotNull Pageable pageable, @NotBlank String role);
}
