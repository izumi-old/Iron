package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.dto.PersonDto;
import pet.kozhinov.iron.exception.BadRequestException;
import pet.kozhinov.iron.repository.PersonRepository;

import java.util.HashSet;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.utils.ValidationUtils.validateId;

@RequiredArgsConstructor
@Component(PersonMapper.NAME)
public class PersonMapper implements Mapper<Person, PersonDto> {
    public static final String NAME = "iron_PersonMapper";

    private final PersonRepository personRepository;
    private final RoleMapper roleMapper;

    @Override
    public PersonDto map1(Person from) {
        PersonDto dto = new PersonDto();
        dto.setId(from.getId().toString());
        dto.setEmail(from.getEmail());
        dto.setPhoneNumber(from.getPhoneNumber());
        dto.setFirstName(from.getFirstName());
        dto.setLastName(from.getLastName());
        dto.setPatronymic(from.getPatronymic());
        dto.setPassportSeriesAndNumber(from.getPassportSeriesAndNumber());
        dto.setBanned(from.getBanned());
        dto.setLatestSignInDate(from.getLatestSignInDate());
        dto.setRoles(from.getRoles().stream()
                .map(roleMapper::map1)
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public Person map2(PersonDto to) {
        Person person;
        if (to.getId() == null) {
            person = new Person();
            person.setRoles(new HashSet<>());
        } else {
            validateId(to.getId());

            person = personRepository.findById(Long.parseLong(to.getId()))
                .orElseThrow(() -> new BadRequestException("Id is specified, but no entity with such id was found"));
        }

        if (to.getFirstName() != null) {
            person.setFirstName(to.getFirstName());
        }
        if (to.getLastName() != null) {
            person.setLastName(to.getLastName());
        }
        if (to.getPatronymic() != null) {
            person.setPatronymic(to.getPatronymic());
        }
        if (to.getEmail() != null) {
            person.setEmail(to.getEmail());
        }
        if (to.getPhoneNumber() != null) {
            person.setPhoneNumber(to.getPhoneNumber());
        }
        if (to.getPassword() != null) {
            person.setPassword(to.getPassword());
        }
        if (to.getPassportSeriesAndNumber() != null) {
            person.setPassportSeriesAndNumber(to.getPassportSeriesAndNumber());
        }
        if (to.getBanned() != null) {
            person.setBanned(to.getBanned());
        }
        return person;
    }
}
