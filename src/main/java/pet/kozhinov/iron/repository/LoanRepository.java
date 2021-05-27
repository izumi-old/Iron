package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
