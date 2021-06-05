package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.Role;

import java.util.Collection;
import java.util.Optional;

@Repository(PersonRepository.NAME)
public interface PersonRepository extends JpaRepository<Person, Long> {
    String NAME = "iron_PersonRepository";

    long countAllByRolesContaining(Role role);

    Optional<Person> findByEmail(String email);
    Optional<Person> findByPhoneNumber(String phoneNumber);
    Collection<Person> findAllByRolesContaining(Role role);
}
