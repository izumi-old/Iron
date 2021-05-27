package pet.kozhinov.iron.service;

import pet.kozhinov.iron.entity.dto.CaseDto;

import java.util.Collection;
import java.util.Optional;

public interface CaseService {
    Collection<CaseDto> getAllPending();
    Collection<CaseDto> getAllAcceptedInProgress();
    Collection<CaseDto> getAllPendingForClient(String clientId);
    Collection<CaseDto> getAllAcceptedForClient(String clientId);
    Optional<CaseDto> getById(String id);
    CaseDto update(String id, CaseDto loanCase);
    CaseDto save(CaseDto loanOffer);
    void delete(String id);
}
