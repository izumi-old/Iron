package pet.kozhinov.iron.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pet.kozhinov.iron.entity.Person;
import pet.kozhinov.iron.exception.AuthorizationException;
import pet.kozhinov.iron.exception.InternalServerErrorException;
import pet.kozhinov.iron.service.PersonService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static pet.kozhinov.iron.utils.Constants.SECURITY_HEADER_STRING;
import static pet.kozhinov.iron.utils.Constants.SECURITY_SECRET;
import static pet.kozhinov.iron.utils.Constants.SECURITY_TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final PersonService personService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, PersonService personService) {
        super(authenticationManager);
        this.personService = personService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            String header = request.getHeader(SECURITY_HEADER_STRING);
            if (header == null || !header.startsWith(SECURITY_TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new AuthorizationException(e);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SECURITY_HEADER_STRING);
        if (token == null ) {
            return null;
        }

        String username = parseToken(token);
        Person person = personService.getByLogin(username)
                .orElseThrow(() -> new InternalServerErrorException("Person by login wasn't found, but it expected"));
        return new UsernamePasswordAuthenticationToken(person, null, person.getRoles());
    }

    private String parseToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECURITY_SECRET.getBytes()))
                .build()
                .verify(token.replace(SECURITY_TOKEN_PREFIX, ""))
                .getSubject();
    }
}
