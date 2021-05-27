package pet.kozhinov.iron.service;

import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.dto.CaseDto;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;

@Validated
public interface CaseService {
    Collection<CaseDto> getAllPending();
    Collection<CaseDto> getAllAcceptedInProgress();
    Collection<CaseDto> getAllPendingForClient(@NotBlank String clientId);
    Collection<CaseDto> getAllAcceptedForClient(@NotBlank String clientId);
    Optional<CaseDto> getById(@NotBlank String id);
    CaseDto update(CaseDto loanCase);
    CaseDto save(CaseDto loanOffer);
    void delete(@NotBlank String id);
}