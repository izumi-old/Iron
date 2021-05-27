package pet.kozhinov.iron.mapper;

import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.dto.PersonDto;

@Component
public class PersonMapper implements Mapper<Person, PersonDto> {

    @Override
    public PersonDto map1(Person from) {
        PersonDto dto = new PersonDto();
        dto.setId(from.getId().toString());
        dto.setEmail(from.getEmail());
        dto.setPhoneNumber(from.getPhoneNumber());
        dto.setFirstName(from.getFirstName());
        dto.setLastName(from.getLastName());
        dto.setPatronymic(from.getPatronymic());
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
        return person;
    }
}
