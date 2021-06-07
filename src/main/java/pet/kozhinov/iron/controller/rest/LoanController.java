package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.service.LoanService;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.DEFAULT_PAGE_SIZE;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX)
@RestController(LoanController.NAME)
public class LoanController {
    public static final String NAME = "LoanController";
    private final LoanService loanService;

    @GetMapping("/loans")
    public Page<LoanDto> getLoans(final @RequestParam Integer page,
                                  final @RequestParam(required = false) Integer size) {
        int guaranteedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        return loanService.getLoans(PageRequest.of(page, guaranteedSize));
    }
}
