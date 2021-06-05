package pet.kozhinov.iron.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public interface CaseService {

    Page<CaseDto> getCases(@NotNull Pageable pageable);
    Page<CaseDto> getCasesByPersonId(@NotNull Pageable pageable,
                                     @NotNull Long clientId);

    Page<CaseDto> getCasesByStatusesAndPersonId(@NotNull Pageable pageable,
                                                @NotNull Status bankStatus,
                                                @NotNull Status clientStatus);

    Page<CaseDto> getCasesByStatusesAndPersonId(@NotNull Pageable pageable,
                                                @NotNull Long clientId,
                                                @NotNull Status bankStatus,
                                                @NotNull Status clientStatus);

    CaseDto update(CaseDto loanCase);
    CaseDto save(CaseDto loanOffer);
    void delete(@NotBlank String id);
}
