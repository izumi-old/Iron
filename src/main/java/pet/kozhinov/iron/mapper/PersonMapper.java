package pet.kozhinov.iron.mapper;

import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.entity.dto.PersonDto;

import java.util.stream.Collectors;

@Component(PersonMapper.NAME)
public class PersonMapper implements Mapper<Person, PersonDto> {
    public static final String NAME = "iron_PersonMapper";

    @Override
    public PersonDto map1(Person from) {
        PersonDto dto = new PersonDto();
        dto.setId(from.getId().toString());
        dto.setEmail(from.getEmail());
        dto.setPhoneNumber(from.getPhoneNumber());
        dto.setFirstName(from.getFirstName());
        dto.setLastName(from.getLastName());
        dto.setPatronymic(from.getPatronymic());
        dto.setPassportNumberAndSeries(from.getPassportNumberAndSeries());
        dto.setRoles(from.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public Person map2(PersonDto to) {
        Person person = new Person();
        person.setId(to.getId() != null ? Long.parseLong(to.getId()) : null);
        person.setFirstName(to.getFirstName());
        person.setLastName(to.getLastName());
        person.setEmail(to.getEmail());
        person.setPhoneNumber(to.getPhoneNumber());
        person.setPassword(to.getPassword());
        person.setPatronymic(to.getPatronymic());
        person.setPassportNumberAndSeries(to.getPassportNumberAndSeries());
        return person;
    }
}
