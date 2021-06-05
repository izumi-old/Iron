package pet.kozhinov.iron.utils;

import org.springframework.security.core.GrantedAuthority;
import pet.kozhinov.iron.entity.Role;

import java.util.Collection;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static boolean hasRole(String role, Collection<? extends GrantedAuthority> authorities) {
        if (role == null || authorities == null || role.length() == 0 || authorities.size() == 0) {
            return false;
        }

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasRole(Role.Default role, Collection<? extends GrantedAuthority> authorities) {
        return hasRole(role.getName(), authorities);
    }

    public static boolean hasAnyRole(Collection<? extends GrantedAuthority> authorities, Role.Default... roles) {
        boolean result = false;
        for (Role.Default role : roles) {
            result |= hasRole(role.toString(), authorities);
        }
        return result;
    }
}
