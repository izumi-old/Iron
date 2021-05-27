package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.entity.dto.PersonDto;
import pet.kozhinov.iron.exception.ForbiddenException;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.service.CaseService;
import pet.kozhinov.iron.service.PersonService;

import javax.validation.constraints.NotBlank;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.CURRENT_PERSON_KEYWORD;
import static pet.kozhinov.iron.utils.SecurityUtils.hasRole;
import static pet.kozhinov.iron.utils.Utils.toPage;
import static pet.kozhinov.iron.utils.ValidationUtils.validatePagination;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController(PersonController.NAME)
public class PersonController {
    public static final String NAME = "iron_PersonController";
    private final PersonService personService;
    private final CaseService caseService;

    @GetMapping("/person/{id}")
    public PersonDto get(Authentication authentication,
                         @NotBlank @PathVariable String id) {
        Person current = ((Person) authentication.getPrincipal());
        if (id.equals(CURRENT_PERSON_KEYWORD)) {
            id = current.getId().toString();
        } else if (!(hasRole(Role.Default.MANAGER, current.getRoles()) ||
                hasRole(Role.Default.ADMIN, current.getRoles()))) {
            throw new ForbiddenException();
        }

        return personService.getById(id)
                .orElseThrow(() -> new NotFoundException("A person with such id wasn't found"));
    }

    @PostMapping("/person")
    public PersonDto signup(PersonDto personDto) {
        return personService.signup(personDto);
    }

    @GetMapping("/persons")
    public Page<PersonDto> getAll(@RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size) {
        validatePagination(page, size);
        boolean noPagination = page == null;
        if (noPagination) {
            return toPage(personService.getAll());
        }

        return personService.getAll(page, size);
    }

    @GetMapping("/persons/filtered")
    public Page<PersonDto> getAll(@RequestParam String role,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size) {
        validatePagination(page, size);
        boolean noPagination = page == null;
        if (noPagination) {
            return toPage(personService.getAllByRole(role.toUpperCase()));
        }

        return personService.getAllByRole(page, size, role.toUpperCase());
    }

    @GetMapping("/person/{personId}/loans")
    public Page<CaseDto> getLoans(Authentication authentication,
                                  @NotBlank @PathVariable String personId,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size) {
        validatePagination(page, size);
        Person current = ((Person) authentication.getPrincipal());
        if (personId.equals(CURRENT_PERSON_KEYWORD)) {
            personId = current.getId().toString();
        } else if (!(hasRole(Role.Default.MANAGER, current.getRoles()) ||
                hasRole(Role.Default.ADMIN, current.getRoles()))) {
            throw new ForbiddenException();
        }

        boolean noPagination = page == null;
        if (noPagination) {
            return toPage(caseService.getAllAcceptedForClient(personId));
        }

        return caseService.getAllAcceptedForClient(page, size, personId);
    }

    @GetMapping("/person/{personId}/offers")
    public Page<CaseDto> getLoanOffers(Authentication authentication,
                                             @NotBlank @PathVariable String personId,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size) {
        validatePagination(page, size);
        Person current = ((Person) authentication.getPrincipal());
        if (personId.equals(CURRENT_PERSON_KEYWORD)) {
            personId = current.getId().toString();
        } else if (!(hasRole(Role.Default.MANAGER, current.getRoles()) ||
                hasRole(Role.Default.ADMIN, current.getRoles()))) {
            throw new ForbiddenException();
        }

        boolean noPagination = page == null;
        if (noPagination) {
            return toPage(caseService.getAllPendingForClient(personId));
        }

        return caseService.getAllPendingForClient(page, size, personId);
    }
}
