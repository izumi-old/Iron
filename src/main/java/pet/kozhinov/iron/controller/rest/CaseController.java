package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.service.CaseService;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/loan-cases")
@RestController
public class CaseController {
    private final CaseService caseService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CaseDto post(@RequestBody CaseDto dto) {
        return caseService.save(dto);
    }

    @GetMapping("/pending")
    public Collection<CaseDto> getAllPendingOffers() {
        return caseService.getAllPending();
    }

    @GetMapping("/in-progress")
    public Collection<CaseDto> getAllAcceptedInProgressOfPaying() {
        return caseService.getAllAcceptedInProgress();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NotBlank @PathVariable String id) {
        caseService.delete(id);
    }
}
