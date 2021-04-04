package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Loan;

import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {
}
