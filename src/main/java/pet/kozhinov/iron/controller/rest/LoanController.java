package pet.kozhinov.iron.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.service.LoanService;

import java.util.Collection;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;

@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/loans")
@RestController
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/")
    public Collection<LoanDto> getAll() {
        return loanService.getAll();
    }
}
