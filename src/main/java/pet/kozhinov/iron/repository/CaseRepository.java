package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Status;

import java.util.Collection;

@Repository(CaseRepository.NAME)
public interface CaseRepository extends JpaRepository<Case, Long> {
    String NAME = "iron_CaseRepository";

    Collection<Case> findAllByStatusBankSideAndStatusClientSideAndClosed(
            Status statusBankSide, Status statusClientSide, boolean closed);

    Collection<Case> findAllByClientIdAndStatusBankSideAndStatusClientSide(
            Long clientId, Status statusBankSide, Status statusClientSide);
}
