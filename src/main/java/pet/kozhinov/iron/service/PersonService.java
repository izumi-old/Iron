package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.Person;

import java.util.Collection;
import java.util.Optional;

public interface PersonService {
    Optional<Person> getById(String id);
    Optional<Person> getByLogin(String login);
    Optional<Person> getByEmail(String email);
    Optional<Person> getByPhoneNumber(String phoneNumber);
    Collection<Person> getByRole(String role);
}
