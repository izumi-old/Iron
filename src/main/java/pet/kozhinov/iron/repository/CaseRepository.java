package pet.kozhinov.iron.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Status;

@Repository(CaseRepository.NAME)
public interface CaseRepository extends JpaRepository<Case, Long> {
    String NAME = "iron_CaseRepository";

    Page<Case> findAllByClientId(Pageable pageable, Long clientId);
    Page<Case> findAllByStatusBankSideAndStatusClientSideAndClosed(
            Pageable pageable, Status statusBankSide, Status statusClientSide, boolean closed);
    Page<Case> findAllByClientIdAndStatusBankSideAndStatusClientSide(
            Pageable pageable, Long clientId, Status statusBankSide, Status statusClientSide);
}
