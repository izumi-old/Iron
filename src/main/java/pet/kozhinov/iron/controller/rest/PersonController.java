package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

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
    public PersonDto get(Authentication authentication, @NotNull @NotBlank @PathVariable String id) {
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
    public Collection<PersonDto> getClients() {
        return personService.getAll();
    }

    @GetMapping("/persons/filtered")
    public Collection<PersonDto> getClients(@RequestParam String role) {
        return personService.getAllByRole(role.toUpperCase());
    }

    @GetMapping("/person/{id}/loans")
    public Collection<CaseDto> getLoans(@NotBlank @PathVariable String id, Authentication authentication) {
        if (id != null && id.equals(CURRENT_PERSON_KEYWORD)) {
            id = ((Person) authentication.getPrincipal()).getId().toString();
        }
        return caseService.getAllAcceptedForClient(id);
    }

    @GetMapping("/person/{id}/offers")
    public Collection<CaseDto> getLoanOffers(@NotBlank @PathVariable String id, Authentication authentication) {
        if (id != null && id.equals(CURRENT_PERSON_KEYWORD)) {
            id = ((Person) authentication.getPrincipal()).getId().toString();
        }
        return caseService.getAllPendingForClient(id);
    }

    @PatchMapping("/person//{id}/offers/{loanId}")
    public CaseDto updateLoanCase(@NotBlank @PathVariable String id, @NotBlank @PathVariable String loanId,
                                  @NotBlank @RequestParam String newStatus, Authentication authentication) {
        if (id != null && id.equals(CURRENT_PERSON_KEYWORD)) {
            id = ((Person) authentication.getPrincipal()).getId().toString();
        }

        Optional<CaseDto> optional = caseService.getById(loanId);
        if (optional.isEmpty()) {
            throw new NotFoundException();
        }

        CaseDto loanCase = optional.get();
        if (!loanCase.getClientId().equals(id)) {
            throw new ForbiddenException();
        }

        loanCase.setStatusClientSide(newStatus);
        return caseService.update(loanId, loanCase);
    }
}
