package pet.kozhinov.iron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.kozhinov.iron.entity.Payment;

@Repository(PaymentRepository.NAME)
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    String NAME = "iron_PaymentRepository";
}
