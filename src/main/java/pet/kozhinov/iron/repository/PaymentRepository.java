package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
