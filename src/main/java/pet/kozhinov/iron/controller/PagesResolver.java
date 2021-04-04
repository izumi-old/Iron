package pet.kozhinov.iron.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pet.kozhinov.iron.entity.Role;
import pet.kozhinov.iron.security.PersonDetails;

import java.util.Collection;

import static pet.kozhinov.iron.utils.Constants.API_PREFIX;
import static pet.kozhinov.iron.utils.SecurityUtils.hasRole;

@Controller
public class PagesResolver {

    @GetMapping("/")
    public ModelAndView showWelcomePage(Authentication authentication) {
        ModelAndView mav = new ModelAndView();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean client = hasRole(Role.Default.CLIENT, authorities);
        boolean manager = hasRole(Role.Default.MANAGER, authorities);
        boolean admin = hasRole(Role.Default.ADMIN, authorities);
        mav.addObject("client", client);
        mav.addObject("manager", manager);
        mav.addObject("admin", admin);

        putUsername(authentication, mav);

        if (client && !manager && !admin) {
            mav.setViewName("client");
            return mav;
        } else if (!client && manager && !admin) {
            mav.setViewName("manager");
            return mav;
        } else if (!client && !manager && admin) {
            mav.setViewName("admin");
            return mav;
        }

        mav.setViewName("index");
        return mav;
    }

    @GetMapping(API_PREFIX + "/client")
    public ModelAndView clientIndex(Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        putUsername(authentication, mav);
        mav.setViewName("client");
        return mav;
    }

    @GetMapping(API_PREFIX + "/manager")
    public String managerIndex() {
        return "manager";
    }

    @GetMapping(API_PREFIX + "/admin")
    public String adminIndex() {
        return "admin";
    }

    private void putUsername(Authentication authentication, ModelAndView mav) {
        PersonDetails details = (PersonDetails) authentication.getPrincipal();
        String username;
        if (details.getPatronymic() != null) {
            username = String.format("%s %s %s", details.getLastName(), details.getFirstName(), details.getPatronymic());
        } else {
            username = String.format("%s %s", details.getFirstName(), details.getLastName());
        }
        mav.addObject("username", username);
    }
}