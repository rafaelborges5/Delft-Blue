package nl.tudelft.sem.template.authentication.domain.user;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.authentication.domain.HasEvents;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "password_hash", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @Column(name = "role", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    @Column(name = "faculty", nullable = false)
    @Convert(converter = FacultyAttributeConverter.class)
    private List<FacultyName> faculty;

    /**
     * Create new application user.
     *
     * @param netId    The NetId for the new user
     * @param password The password for the new user
     * @param role     The role of the new user
     * @param faculty   The list of faculties of the new user
     */
    public AppUser(NetId netId, HashedPassword password, Role role, List<FacultyName> faculty) {
        this.netId = netId;
        this.password = password;
        this.role = role;
        this.faculty = faculty;
        this.recordThat(new UserWasCreatedEvent(netId));
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    /**
     * Assigns a user a new faculty alongside their existing ones.
     *
     * @param newFaculty  A new faculty the user also belongs to
     */
    public void addFaculty(FacultyName newFaculty) {
        this.faculty.add(newFaculty);
    }

    /**
     * Return the list of faculties the user belongs to as a string.
     *
     * @return String with faculties
     */
    public String facultyString() {
        String faculties = this.faculty.toString();
        faculties = faculties.replace("[", "");
        faculties = faculties.replace("]", "");
        return faculties;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "NetId = " + netId.toString() +
                ", password = " + password.toString() +
                ", role = " + role.name() +
                ", faculties = " + facultyString() +
                "}";
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return id == (appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netId);
    }
}
