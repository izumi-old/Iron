package pet.kozhinov.iron.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;

@Data
@Table
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public enum Default {
        CLIENT, MANAGER, ADMIN
    }
}
