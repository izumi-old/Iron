package pet.kozhinov.iron.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Table(name = "person")
@Entity
public class Person {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @NotEmpty
    @Size(min = 1, max = 64)
    @Column(nullable = false)
    private String firstName;

    @NotEmpty
    @Size(min = 1, max = 64)
    @Column(nullable = false)
    private String lastName;

    @Size(min = 1, max = 64)
    private String patronymic;

    @NotEmpty
    @Size(min = 1, max = 64)
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "\\+7\\(\\d\\d\\d\\)\\d\\d\\d-\\d\\d-\\d\\d") /* like +7(123)456-78-90 */
    @Column(unique = true)
    private String phoneNumber;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Pattern(regexp = "\\d\\d\\d\\d \\d\\d\\d\\d\\d\\d") /* like 1234 567890 */
    @Column(nullable = false, unique = true)
    private String passportNumberAndSeries;

    @Transient
    public String getLogin() {
        return email != null ? email : phoneNumber;
    }
}
