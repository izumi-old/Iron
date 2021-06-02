package pet.kozhinov.iron.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Table(name = "role")
@Entity
public class Role implements GrantedAuthority {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column
    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy="roles")
    public Set<Person> persons;

    @Override
    public String getAuthority() {
        return name;
    }

    public enum Default {
        CLIENT, MANAGER, ADMIN
    }
}
