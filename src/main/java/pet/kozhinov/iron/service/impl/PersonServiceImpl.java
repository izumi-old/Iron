package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.entity.dto.PersonDto;
import pet.kozhinov.iron.mapper.Mapper;
import pet.kozhinov.iron.repository.PersonRepository;
import pet.kozhinov.iron.service.PersonService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository repository;
    private final Mapper<Person, PersonDto> mapper;

    @Override
    public PersonDto signup(PersonDto personDto) {
        Person toSave = mapper.map2(personDto);
        Person saved = repository.save(toSave);
        return mapper.map1(saved);
    }

    @Override
    public Optional<PersonDto> getById(String id) {
        return repository.findById(Long.parseLong(id))
                .map(mapper::map1);
    }

    @Override
    public Optional<Person> getByLogin(String login) {
        Optional<Person> optional = getByEmail(login);
        if (optional.isPresent()) {
            return optional;
        }

        return getByPhoneNumber(login);
    }

    @Override
    public Optional<Person> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<Person> getByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Collection<PersonDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<PersonDto> getAllByRole(String role) {
        Collection<Person> result = new ArrayList<>();
        repository.findAll().forEach(person -> {
            for (Role role0 : person.getRoles()) {
                if (role0.getName().equals(role)) {
                    result.add(person);
                    break;
                }
            }
        });

        return result.stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }
}
