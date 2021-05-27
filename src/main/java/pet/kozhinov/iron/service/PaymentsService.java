package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Payment;

import java.util.Collection;

public interface PaymentsService {
    Collection<Payment> schedule(Case aCase);
}
