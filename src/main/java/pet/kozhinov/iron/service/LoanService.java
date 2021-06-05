package pet.kozhinov.iron.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.dto.LoanDto;

import javax.validation.constraints.NotNull;

@Validated
public interface LoanService {
    Page<LoanDto> getLoans(@NotNull Pageable pageable);
}
