package pet.kozhinov.iron.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pet.kozhinov.iron.entity.Person;

import java.util.Collection;
import java.util.UUID;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return person.getRoles();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getEmail() != null ? person.getEmail() : person.getPhoneNumber();
    }

    public String getFirstName() {
        return person.getFirstName();
    }

    public String getLastName() {
        return person.getLastName();
    }

    public String getPatronymic() {
        return person.getPatronymic();
    }

    public UUID getId() {
        return person.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
