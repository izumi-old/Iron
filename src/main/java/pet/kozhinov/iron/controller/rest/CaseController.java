package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.exception.BadRequestException;
import pet.kozhinov.iron.service.CaseService;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController
public class CaseController {
    private final CaseService caseService;

    @PostMapping(value = "/cases")
    public CaseDto create(@RequestBody CaseDto dto) {
        return caseService.save(dto);
    }

    @GetMapping("/cases/filtered")
    public Collection<CaseDto> getAllPendingOffers(@RequestParam String filterStatus) {
        if (filterStatus.equals("pending")) {
            return caseService.getAllPending();
        } else if (filterStatus.equals("in-progress")) {
            return caseService.getAllAcceptedInProgress();
        } else {
            throw new BadRequestException("Incorrect/unknown filter parameter");
        }
    }

    @PatchMapping("/case")
    public CaseDto update(CaseDto toUpdate) {
        return caseService.update(toUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NotBlank @PathVariable String id) {
        caseService.delete(id);
    }
}
