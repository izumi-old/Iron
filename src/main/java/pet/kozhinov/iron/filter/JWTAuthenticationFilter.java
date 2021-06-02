package pet.kozhinov.iron.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pet.kozhinov.iron.entity.dto.PersonDto;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static pet.kozhinov.iron.utils.Constants.API_LOGIN_URL;
import static pet.kozhinov.iron.utils.Constants.SECURITY_EXPIRATION_TIME_MILLISECONDS;
import static pet.kozhinov.iron.utils.Constants.SECURITY_HEADER_STRING;
import static pet.kozhinov.iron.utils.Constants.SECURITY_SECRET;
import static pet.kozhinov.iron.utils.Constants.SECURITY_TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(API_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            PersonDto creds = objectMapper.readValue(request.getInputStream(), PersonDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (AuthenticationException | IOException e) {
            throw new pet.kozhinov.iron.exception.AuthenticationException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication auth) {
        User currentUser = (User) auth.getPrincipal();
        Date expiresAt = new Date(System.currentTimeMillis() + SECURITY_EXPIRATION_TIME_MILLISECONDS);

        String token = JWT.create()
                .withSubject(currentUser.getUsername())
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(SECURITY_SECRET.getBytes()));
        response.addHeader(SECURITY_HEADER_STRING, SECURITY_TOKEN_PREFIX + token);
    }
}
