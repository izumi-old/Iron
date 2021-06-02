package pet.kozhinov.iron.entity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pet.kozhinov.iron.entity.Person;

import java.util.Collection;

@RequiredArgsConstructor
public class PersonDetails implements UserDetails {
    private final Person person;

    public Long getId() {
        return person.getId();
    }

    @Override
    public String getUsername() {
        return person.getLogin();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return person.getRoles();
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
