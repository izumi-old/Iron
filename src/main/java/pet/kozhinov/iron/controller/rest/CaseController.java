package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.entity.security.PersonDetails;
import pet.kozhinov.iron.exception.ForbiddenException;
import pet.kozhinov.iron.service.CaseService;

import javax.validation.constraints.NotBlank;

import static pet.kozhinov.iron.entity.Role.Default.ADMIN;
import static pet.kozhinov.iron.entity.Role.Default.MANAGER;
import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.CURRENT_PERSON_KEYWORD;
import static pet.kozhinov.iron.utils.Constants.DEFAULT_PAGE_SIZE;
import static pet.kozhinov.iron.utils.SecurityUtils.hasAnyRole;
import static pet.kozhinov.iron.utils.ValidationUtils.validateId;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController(CaseController.NAME)
public final class CaseController {
    public static final String NAME = "iron_CaseController";
    private final CaseService caseService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/case")
    public CaseDto create(final @RequestBody CaseDto dto) {
        return caseService.save(dto);
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @GetMapping("/cases")
    public Page<CaseDto> getCases(final @RequestParam Integer page,
                                  final @RequestParam(required = false) Integer size) {
        int guaranteedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        return caseService.getCases(PageRequest.of(page, guaranteedSize));
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @GetMapping("/cases/filtered")
    public Page<CaseDto> getCasesFiltered(final @RequestParam Integer page,
                                          final @RequestParam(required = false) Integer size,
                                          final @RequestParam String bankStatus,
                                          final @RequestParam String clientStatus) {
        int guaranteedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        Status bankStatus0 = Status.valueOf(bankStatus.toUpperCase());
        Status clientStatus0 = Status.valueOf(clientStatus.toUpperCase());
        return caseService.getCasesByStatusesAndPersonId(PageRequest.of(page, guaranteedSize),
                bankStatus0, clientStatus0);
    }

    @GetMapping("persons/{personId}/cases")
    public Page<CaseDto> getPersonCases(final Authentication authentication,
                                        final @RequestParam Integer page,
                                        final @RequestParam(required = false) Integer size,
                                        final @PathVariable String personId) {
        long personId0 = getPersonId0(authentication, personId);
        int guaranteedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        return caseService.getCasesByPersonId(PageRequest.of(page, guaranteedSize), personId0);
    }

    @GetMapping("persons/{personId}/cases/filtered")
    public Page<CaseDto> getPersonCasesFiltered(final Authentication authentication,
                                                final @RequestParam Integer page,
                                                final @RequestParam(required = false) Integer size,
                                                final @PathVariable String personId,
                                                final @RequestParam String bankStatus,
                                                final @RequestParam String clientStatus) {
        long personId0 = getPersonId0(authentication, personId);
        int guaranteedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        Status bankStatus0 = Status.valueOf(bankStatus.toUpperCase());
        Status clientStatus0 = Status.valueOf(clientStatus.toUpperCase());
        return caseService.getCasesByStatusesAndPersonId(PageRequest.of(page, guaranteedSize),
                personId0, bankStatus0, clientStatus0);
    }

    @PatchMapping("/case")
    public CaseDto update(final @RequestBody CaseDto toUpdate) {
        return caseService.update(toUpdate);
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final @NotBlank @PathVariable String id) {
        caseService.delete(id);
    }

    private long getPersonId0(final Authentication authentication,
                              final @PathVariable String personId) {
        PersonDetails current = ((PersonDetails) authentication.getDetails());
        long personId0;
        if (personId.equals(CURRENT_PERSON_KEYWORD)) {
            personId0 = current.getId();
        } else if (!hasAnyRole(current.getAuthorities(), MANAGER, ADMIN)) {
            throw new ForbiddenException();
        } else {
            validateId(personId);
            personId0 = Long.parseLong(personId);
        }
        return personId0;
    }
}
