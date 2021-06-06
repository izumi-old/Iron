package pet.kozhinov.iron.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.entity.security.PersonDetails;
import pet.kozhinov.iron.exception.AuthorizationException;
import pet.kozhinov.iron.exception.ForbiddenException;
import pet.kozhinov.iron.exception.InternalServerErrorException;
import pet.kozhinov.iron.repository.PersonRepository;
import pet.kozhinov.iron.service.PersonService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

import static pet.kozhinov.iron.utils.Constants.SECURITY_HEADER_STRING;
import static pet.kozhinov.iron.utils.Constants.SECURITY_SECRET;
import static pet.kozhinov.iron.utils.Constants.SECURITY_TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final PersonService personService;
    private final PersonRepository personRepository;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, PersonService personService,
                                  PersonRepository personRepository) {
        super(authenticationManager);
        this.personService = personService;
        this.personRepository = personRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            String header = request.getHeader(SECURITY_HEADER_STRING);
            if (header == null || !header.startsWith(SECURITY_TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }

            String username = getUsername(header);
            Person person = personService.getPersonByLogin(username)
                .orElseThrow(() -> new InternalServerErrorException(
                        "Person by login wasn't found, but it was expected"));

            if (person.isBanned()) {
                throw new ForbiddenException();
            }

            person.setLatestSignInDate(LocalDate.now());
            person = personRepository.save(person);

            Authentication authentication = getAuthentication(person);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new AuthorizationException(e);
        }
    }

    private AbstractAuthenticationToken getAuthentication(Person person) {
        Object principal = person.getLogin();
        Object credentials = person.getPassword();
        Collection<? extends GrantedAuthority> authorities = person.getRoles();
        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        authentication.setDetails(new PersonDetails(person));

        return authentication;
    }

    private String getUsername(String token) {
        return JWT.require(Algorithm.HMAC512(SECURITY_SECRET.getBytes()))
                .build()
                .verify(token.replace(SECURITY_TOKEN_PREFIX, ""))
                .getSubject();
    }
}
