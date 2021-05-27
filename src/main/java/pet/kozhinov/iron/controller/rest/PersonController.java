package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
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
import java.util.Collection;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.CURRENT_PERSON_KEYWORD;
import static pet.kozhinov.iron.utils.SecurityUtils.hasRole;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController
public class PersonController {
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
    public Collection<PersonDto> getAll() {
        return personService.getAll();
    }

    @GetMapping("/persons/filtered")
    public Collection<PersonDto> getAll(@RequestParam String role) {
        return personService.getAllByRole(role.toUpperCase());
    }

    @GetMapping("/person/{personId}/loans")
    public Collection<CaseDto> getLoans(Authentication authentication,
                                        @NotBlank @PathVariable String personId) {
        Person current = ((Person) authentication.getPrincipal());
        if (personId.equals(CURRENT_PERSON_KEYWORD)) {
            personId = current.getId().toString();
        } else if (!(hasRole(Role.Default.MANAGER, current.getRoles()) ||
                hasRole(Role.Default.ADMIN, current.getRoles()))) {
            throw new ForbiddenException();
        }
        return caseService.getAllAcceptedForClient(personId);
    }

    @GetMapping("/person/{personId}/offers")
    public Collection<CaseDto> getLoanOffers(Authentication authentication,
                                             @NotBlank @PathVariable String personId) {
        Person current = ((Person) authentication.getPrincipal());
        if (personId.equals(CURRENT_PERSON_KEYWORD)) {
            personId = current.getId().toString();
        } else if (!(hasRole(Role.Default.MANAGER, current.getRoles()) ||
                hasRole(Role.Default.ADMIN, current.getRoles()))) {
            throw new ForbiddenException();
        }
        return caseService.getAllPendingForClient(personId);
    }
}
