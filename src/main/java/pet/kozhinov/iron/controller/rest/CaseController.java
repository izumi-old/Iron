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
public class CaseController {
    public static final String NAME = "iron_CaseController";
    private final CaseService caseService;

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @PostMapping(value = "/case")
    public CaseDto create(@RequestBody CaseDto dto) {
        return caseService.save(dto);
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @GetMapping("/cases")
    public Page<CaseDto> getCases(@RequestParam Integer page,
                                  @RequestParam(required = false) Integer size) {
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        return caseService.getCases(PageRequest.of(page, size));
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @GetMapping("/cases/filtered")
    public Page<CaseDto> getCasesFiltered(@RequestParam Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam String bankStatus,
                                        @RequestParam String clientStatus) {
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        Status bankStatus0 = Status.valueOf(bankStatus.toUpperCase());
        Status clientStatus0 = Status.valueOf(clientStatus.toUpperCase());
        return caseService.getCasesByStatusesAndPersonId(PageRequest.of(page, size), bankStatus0, clientStatus0);
    }

    @GetMapping("persons/{personId}/cases")
    public Page<CaseDto> getPersonCases(Authentication authentication,
                                        @RequestParam Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @PathVariable String personId) {
        long personId0 = getPersonId0(authentication, personId);
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        return caseService.getCasesByPersonId(PageRequest.of(page, size), personId0);
    }

    @GetMapping("persons/{personId}/cases/filtered")
    public Page<CaseDto> getPersonCasesFiltered(Authentication authentication,
                                                @RequestParam Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @PathVariable String personId,
                                                @RequestParam String bankStatus,
                                                @RequestParam String clientStatus) {
        long personId0 = getPersonId0(authentication, personId);
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        Status bankStatus0 = Status.valueOf(bankStatus.toUpperCase());
        Status clientStatus0 = Status.valueOf(clientStatus.toUpperCase());
        return caseService.getCasesByStatusesAndPersonId(PageRequest.of(page, size), personId0, bankStatus0, clientStatus0);
    }

    @PatchMapping("/case")
    public CaseDto update(CaseDto toUpdate) {
        return caseService.update(toUpdate);
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NotBlank @PathVariable String id) {
        caseService.delete(id);
    }

    private long getPersonId0(Authentication authentication, @PathVariable String personId) {
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
