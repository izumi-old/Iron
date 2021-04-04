package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.mapper.LoanMapper;
import pet.kozhinov.iron.repository.LoanRepository;
import pet.kozhinov.iron.service.LoanService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository repository;
    private final LoanMapper mapper;

    @Override
    public Optional<Loan> getById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    public Collection<LoanDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
