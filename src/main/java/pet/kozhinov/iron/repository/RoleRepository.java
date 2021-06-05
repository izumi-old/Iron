package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Role;

import java.util.Collection;

@Repository(RoleRepository.NAME)
public interface RoleRepository extends JpaRepository<Role, Long> {
    String NAME = "iron_RoleRepository";

    Collection<Role> findAllByName(String roleName);
}
