package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Utils.toPage;
import static pet.kozhinov.iron.utils.ValidationUtils.validatePagination;

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
    public Page<CaseDto> getAll(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        validatePagination(page, size);
        boolean noPagination = page == null;
        if (noPagination) {
            return toPage(caseService.getAll());
        }

        return caseService.getAll(page, size);
    }

    @PreAuthorize("hasAnyRole('MANAGER, ADMIN')")
    @GetMapping("/cases/filtered")
    public Page<CaseDto> getAllFiltered(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size,
                                @RequestParam String filterStatus) {
        validatePagination(page, size);
        boolean noPagination = page == null;
        if (noPagination) {
            return getAllNoPagination(filterStatus);
        }

        return getAllPagination(page, size, filterStatus);
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

    private Page<CaseDto> getAllNoPagination(String filterStatus) {
        if (filterStatus.equals("pending")) {
            return toPage(caseService.getAllPending());
        } else if (filterStatus.equals("in-progress")) {
            return toPage(caseService.getAllAcceptedInProgress());
        } else {
            throw new BadRequestException("Incorrect/unknown filter parameter");
        }
    }

    private Page<CaseDto> getAllPagination(int page, int size, String filterStatus) {
        if (filterStatus.equals("pending")) {
            return caseService.getAllPending(page, size);
        } else if (filterStatus.equals("in-progress")) {
            return caseService.getAllAcceptedInProgress(page, size);
        } else {
            throw new BadRequestException("Incorrect/unknown filter parameter");
        }
    }
}
