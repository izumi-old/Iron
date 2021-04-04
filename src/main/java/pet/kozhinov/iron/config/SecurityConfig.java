package pet.kozhinov.iron.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pet.kozhinov.iron.entity.Role;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers(API_PREFIX + "/persons/*/loans/**").hasAuthority(Role.Default.CLIENT.toString())
            .antMatchers(API_PREFIX + "/persons/*/offers/**").hasAuthority(Role.Default.CLIENT.toString())
            .antMatchers(API_PREFIX + "/persons/", API_PREFIX + "/loan-cases/**", API_PREFIX + "/loans/**")
                .hasAnyAuthority(Role.Default.MANAGER.toString(), Role.Default.ADMIN.toString())
            .antMatchers(API_PREFIX + "/client/**").hasAuthority(Role.Default.CLIENT.toString())
            .antMatchers(API_PREFIX + "/manager/**").hasAuthority(Role.Default.MANAGER.toString())
            .antMatchers(API_PREFIX + "/admin/**").hasAuthority(Role.Default.ADMIN.toString())
            .anyRequest().authenticated()
        .and()
            .exceptionHandling().accessDeniedPage("/")
        .and()
            .httpBasic()
        .and()
        .logout()
            .logoutUrl("/logout")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .deleteCookies(HttpHeaders.AUTHORIZATION);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}