package pet.kozhinov.iron.service;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.dto.PersonDto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;

@Validated
public interface PersonService {
    PersonDto signup(@Valid PersonDto personDto);
    Optional<PersonDto> getById(@NotBlank String id);
    Optional<Person> getByLogin(@NotBlank String login);
    Optional<Person> getByEmail(@NotBlank @Email String email);
    Optional<Person> getByPhoneNumber(@NotBlank String phoneNumber);

    Collection<PersonDto> getAll();
    Collection<PersonDto> getAllByRole(@NotBlank String role);
    Page<PersonDto> getAll(int page, int size);
    Page<PersonDto> getAllByRole(int page, int size, @NotBlank String role);
}
