package pet.kozhinov.iron.service;

import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.dto.LoanDto;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;

@Validated
public interface LoanService {
    Optional<Loan> getById(@NotBlank String id);
    Collection<LoanDto> getAll();
}
