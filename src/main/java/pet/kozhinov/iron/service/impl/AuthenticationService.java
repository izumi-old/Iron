package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.service.PersonService;

import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {
    private final PersonService personService;

    @Override
    public UserDetails loadUserByUsername(@NotNull String login) throws UsernameNotFoundException {
        log.debug("Loading person by login {{}}", login);
        try {
            Person person = personService.getPersonByEmail(login).orElseGet(
                    () -> personService.getPersonByPhoneNumber(login)
                            .orElseThrow(() -> new UsernameNotFoundException(
                                    String.format("Person with login %s wasn't found", login))));
            log.debug("The person was found");
            return new User(
                    person.getEmail() != null ? person.getEmail() : person.getPhoneNumber(),
                    person.getPassword(),
                    person.getRoles());
        } catch (UsernameNotFoundException ex) {
            log.debug("The person wasn't found");
            throw ex;
        }
    }
}
