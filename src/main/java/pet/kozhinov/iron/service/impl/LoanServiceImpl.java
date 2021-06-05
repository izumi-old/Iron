package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.mapper.LoanMapper;
import pet.kozhinov.iron.repository.LoanRepository;
import pet.kozhinov.iron.service.LoanService;

@RequiredArgsConstructor
@Service(LoanServiceImpl.NAME)
public class LoanServiceImpl implements LoanService {
    public static final String NAME = "iron_LoanServiceImpl";
    private final LoanRepository repository;
    private final LoanMapper mapper;

    @Override
    public Page<LoanDto> getLoans(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map1);
    }
}
