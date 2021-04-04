package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Person;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByEmail(String email);
    Optional<Person> findByPhoneNumber(String phoneNumber);
}
