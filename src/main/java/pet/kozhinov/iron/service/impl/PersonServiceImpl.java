package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.entity.dto.PersonDto;
import pet.kozhinov.iron.mapper.Mapper;
import pet.kozhinov.iron.repository.PersonRepository;
import pet.kozhinov.iron.service.PersonService;
import pet.kozhinov.iron.service.RoleService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.entity.Role.Default.CLIENT;
import static pet.kozhinov.iron.utils.Utils.toPage;

@RequiredArgsConstructor
@Service(PersonServiceImpl.NAME)
public class PersonServiceImpl implements PersonService {
    public static final String NAME = "iron_PersonServiceImpl";
    private final PersonRepository repository;
    private final Mapper<Person, PersonDto> mapper;
    private final RoleService roleService;

    @Override
    public PersonDto signup(PersonDto personDto) {
        Person toSave = mapper.map2(personDto);
        toSave.getRoles().addAll(roleService.getRolesByRoleName(CLIENT.getName()));
        Person saved = repository.save(toSave);
        return mapper.map1(saved);
    }

    @Override
    public Optional<PersonDto> getPersonById(String id) {
        return repository.findById(Long.parseLong(id))
                .map(mapper::map1);
    }

    @Override
    public Optional<Person> getPersonByLogin(String login) {
        Optional<Person> optional = getPersonByEmail(login);
        if (optional.isPresent()) {
            return optional;
        }

        return getPersonByPhoneNumber(login);
    }

    @Override
    public Optional<Person> getPersonByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<Person> getPersonByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<PersonDto> getPersons(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map1);
    }

    @Override
    public Page<PersonDto> getPersonsByRole(Pageable pageable, String role) {
        Collection<Person> result = new LinkedList<>();
        Collection<Role> roles = roleService.getRolesByRoleName(role);
        roles.forEach(role0 -> result.addAll(repository.findAllByRolesContaining(role0)));

        return toPage(result.stream()
                .map(mapper::map1)
                .collect(Collectors.toList()), pageable);
    }

    @Override
    public PersonDto update(PersonDto personDto) {
        Person person = mapper.map2(personDto);
        return mapper.map1(repository.save(person));
    }
}
