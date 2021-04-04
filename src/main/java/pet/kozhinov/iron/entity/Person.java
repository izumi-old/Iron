package pet.kozhinov.iron.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Data
@Table
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
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

    @Pattern(regexp = "\\+7\\(\\d\\d\\d\\)\\d\\d\\d-\\dd-\\d\\d") /* like +7(123)456-78-90 */
    @Column(unique = true)
    private String phoneNumber;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Pattern(regexp = "\\d\\d\\d\\d \\d\\d\\d\\d\\d\\d") /* like 1234 567890 */
    @Column(nullable = false, unique = true)
    private String passportNumberAndSeries;
}
