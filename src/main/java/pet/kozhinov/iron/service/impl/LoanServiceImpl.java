package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.mapper.LoanMapper;
import pet.kozhinov.iron.repository.LoanRepository;
import pet.kozhinov.iron.service.LoanService;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service(LoanServiceImpl.NAME)
public class LoanServiceImpl implements LoanService {
    public static final String NAME = "iron_LoanServiceImpl";
    private final LoanRepository repository;
    private final LoanMapper mapper;

    @Override
    public Collection<LoanDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Page<LoanDto> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(mapper::map1);
    }
}
