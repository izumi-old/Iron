package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pet.kozhinov.iron.entity.dto.PersonDto;
import pet.kozhinov.iron.entity.security.PersonDetails;
import pet.kozhinov.iron.exception.ForbiddenException;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.service.PersonService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static pet.kozhinov.iron.entity.Role.Default.ADMIN;
import static pet.kozhinov.iron.entity.Role.Default.MANAGER;
import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.CURRENT_PERSON_KEYWORD;
import static pet.kozhinov.iron.utils.Constants.DEFAULT_PAGE_SIZE;
import static pet.kozhinov.iron.utils.SecurityUtils.hasAnyRole;
import static pet.kozhinov.iron.utils.SecurityUtils.hasRole;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController(PersonController.NAME)
public class PersonController {
    public static final String NAME = "iron_PersonController";
    private final PersonService personService;

    @GetMapping("/person/{id}")
    public PersonDto get(Authentication authentication,
                         @NotBlank @PathVariable String id) {
        PersonDetails current = ((PersonDetails) authentication.getDetails());
        if (id.equals(CURRENT_PERSON_KEYWORD)) {
            id = current.getId().toString();
        } else if (!hasAnyRole(current.getAuthorities(), MANAGER, ADMIN)) {
            throw new ForbiddenException();
        }

        return personService.getPersonById(id)
                .orElseThrow(() -> new NotFoundException("A person with such id wasn't found"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/person")
    public PersonDto signup(@RequestBody @Valid PersonDto personDto) {
        return personService.signup(personDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/persons")
    public Page<PersonDto> getPersons(@RequestParam Integer page,
                                      @RequestParam(required = false) Integer size) {
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        return personService.getPersons(PageRequest.of(page, size));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/persons/filtered")
    public Page<PersonDto> getPersonsFiltered(Authentication authentication,
                                              @RequestParam String role,
                                              @RequestParam Integer page,
                                              @RequestParam(required = false) Integer size) {
        role = role.toUpperCase();
        if (!role.contains("ROLE_")) {
            role = "ROLE_" + role;
        }

        if (role.equals(MANAGER.getName())) {
            PersonDetails details = ((PersonDetails) authentication.getDetails());
            if (!hasRole(ADMIN, details.getAuthorities())) {
                throw new ForbiddenException();
            }
        }

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        return personService.getPersonsByRole(PageRequest.of(page, size), role);
    }
}
