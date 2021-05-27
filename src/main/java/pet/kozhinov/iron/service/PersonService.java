package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.dto.PersonDto;

import java.util.Collection;
import java.util.Optional;

public interface PersonService {
    PersonDto signup(PersonDto personDto);
    Optional<PersonDto> getById(String id);
    Optional<Person> getByLogin(String login);
    Optional<Person> getByEmail(String email);
    Optional<Person> getByPhoneNumber(String phoneNumber);
    Collection<PersonDto> getAll();
    Collection<PersonDto> getAllByRole(String role);
}
