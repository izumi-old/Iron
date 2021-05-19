package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.LoanCase;
import pet.kozhinov.iron.entity.Payment;

import java.util.Collection;

public interface PaymentsService {
    Collection<Payment> schedule(LoanCase loanCase);
}
