package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.service.LoanService;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Utils.toPage;
import static pet.kozhinov.iron.utils.ValidationUtils.validatePagination;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController(LoanController.NAME)
public class LoanController {
    public static final String NAME = "LoanController";
    private final LoanService loanService;

    @GetMapping("/loans")
    public Page<LoanDto> getAll(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        validatePagination(page, size);
        boolean noPagination = page == null;
        if (noPagination) {
            return toPage(loanService.getAll());
        }

        return loanService.getAll(page, size);
    }
}
