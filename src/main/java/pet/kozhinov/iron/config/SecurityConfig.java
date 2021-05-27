package pet.kozhinov.iron.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.filter.JWTAuthenticationFilter;
import pet.kozhinov.iron.filter.JWTAuthorizationFilter;
import pet.kozhinov.iron.service.PersonService;
import pet.kozhinov.iron.service.impl.AuthenticationService;

import static org.springframework.http.HttpMethod.POST;
import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.Constants.API_SIGNUP_URL;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationService authenticationService;
    private final PersonService personService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers(POST, API_SIGNUP_URL).anonymous()
            .antMatchers(API_PREFIX + "/persons/*/loans/**").hasAuthority(Role.Default.CLIENT.toString())
            .antMatchers(API_PREFIX + "/persons/*/offers/**").hasAuthority(Role.Default.CLIENT.toString())
            .antMatchers(API_PREFIX + "/persons/", API_PREFIX + "/loan-cases/**", API_PREFIX + "/loans/**")
                .hasAnyAuthority(Role.Default.MANAGER.toString(), Role.Default.ADMIN.toString())
            .antMatchers(API_PREFIX + "/client/**").hasAuthority(Role.Default.CLIENT.toString())
            .antMatchers(API_PREFIX + "/manager/**").hasAuthority(Role.Default.MANAGER.toString())
            .antMatchers(API_PREFIX + "/admin/**").hasAuthority(Role.Default.ADMIN.toString())
            .anyRequest().authenticated()
        .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), personService));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(bCryptPasswordEncoder);
    }
}
