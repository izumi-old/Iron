package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.dto.LoanCaseDto;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.exception.ProhibitException;
import pet.kozhinov.iron.security.PersonDetails;
import pet.kozhinov.iron.service.LoanCaseService;
import pet.kozhinov.iron.service.PersonService;

import java.util.Collection;
import java.util.Optional;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.CURRENT_PERSON_KEYWORD;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/persons")
@RestController
public class PersonController {
    private final PersonService personService;
    private final LoanCaseService loanCaseService;

    @GetMapping("/")
    public Collection<Person> getClients(@RequestParam String role) {
        return personService.getByRole(role.toUpperCase());
    }

    @GetMapping("/{id}/loans")
    public Collection<LoanCaseDto> getLoans(@PathVariable String id, Authentication authentication) {
        if (id != null && id.equals(CURRENT_PERSON_KEYWORD)) {
            id = ((PersonDetails) authentication.getPrincipal()).getId().toString();
        }
        return loanCaseService.getAllAcceptedForClient(id);
    }

    @GetMapping("/{id}/offers")
    public Collection<LoanCaseDto> getLoanOffers(@PathVariable String id, Authentication authentication) {
        if (id != null && id.equals(CURRENT_PERSON_KEYWORD)) {
            id = ((PersonDetails) authentication.getPrincipal()).getId().toString();
        }
        return loanCaseService.getAllPendingForClient(id);
    }

    @PatchMapping("/{id}/offers/{loanId}")
    public LoanCaseDto updateLoanCase(@PathVariable String id, @PathVariable String loanId,
                                                  @RequestParam String newStatus, Authentication authentication) {
        if (id != null && id.equals(CURRENT_PERSON_KEYWORD)) {
            id = ((PersonDetails) authentication.getPrincipal()).getId().toString();
        }

        Optional<LoanCaseDto> optional = loanCaseService.getById(loanId);
        if (optional.isEmpty()) {
            throw new NotFoundException();
        }

        LoanCaseDto loanCase = optional.get();
        if (!loanCase.getClientId().equals(id)) {
            throw new ProhibitException();
        }

        loanCase.setStatusClientSide(newStatus);
        return loanCaseService.update(loanId, loanCase);
    }
}
