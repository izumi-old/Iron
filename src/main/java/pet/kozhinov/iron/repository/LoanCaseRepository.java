package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.LoanCase;
import pet.kozhinov.iron.entity.Status;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface LoanCaseRepository extends JpaRepository<LoanCase, UUID> {
    Collection<LoanCase> findAllByStatusBankSideAndStatusClientSideAndClosed(
            Status statusBankSide, Status statusClientSide, boolean closed);

    Collection<LoanCase> findAllByClientIdAndStatusBankSideAndStatusClientSide(
            UUID clientId, Status statusBankSide, Status statusClientSide);
}
