package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.dto.LoanDto;

import java.util.Collection;
import java.util.Optional;

public interface LoanService {
    Optional<Loan> getById(String id);
    Collection<LoanDto> getAll();
}
