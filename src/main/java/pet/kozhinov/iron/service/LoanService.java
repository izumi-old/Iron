package pet.kozhinov.iron.service;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.dto.LoanDto;

import java.util.Collection;

@Validated
public interface LoanService {
    Collection<LoanDto> getAll();
    Page<LoanDto> getAll(int page, int size);
}
