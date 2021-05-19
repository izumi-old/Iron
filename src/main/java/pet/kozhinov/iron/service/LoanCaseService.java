package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.dto.LoanCaseDto;

import java.util.Collection;
import java.util.Optional;

public interface LoanCaseService {
    Collection<LoanCaseDto> getAllPending();
    Collection<LoanCaseDto> getAllAcceptedInProgress();
    Collection<LoanCaseDto> getAllPendingForClient(String clientId);
    Collection<LoanCaseDto> getAllAcceptedForClient(String clientId);
    Optional<LoanCaseDto> getById(String id);
    LoanCaseDto update(String id, LoanCaseDto loanCase);
    LoanCaseDto save(LoanCaseDto loanOffer);
    void delete(String id);
}
