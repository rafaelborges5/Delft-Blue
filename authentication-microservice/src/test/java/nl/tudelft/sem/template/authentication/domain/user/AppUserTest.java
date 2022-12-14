package nl.tudelft.sem.template.authentication.domain.user;

import org.hamcrest.beans.HasProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scala.App;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {
    AppUser user;
    Integer testid;
    NetId testNetID;
    HashedPassword testPassword;
    Role testRole;

    @BeforeEach
    void createUser() {
        testNetID = new NetId("SomeUser");
        testPassword = new HashedPassword("password123");
        testRole = Role.EMPLOYEE;
        user = new AppUser(testNetID, testPassword, testRole);
    }

    @Test
    void getId() {
        assertThat(user.getId()).isEqualTo(0L);
    }

    @Test
    void getNetId() {
        assertThat(user.getNetId()).isEqualTo(testNetID);
    }

    @Test
    void getPassword() {
        assertThat(user.getPassword()).isEqualTo(testPassword);
    }

    @Test
    void getRole() {
        assertThat(user.getRole()).isEqualTo(testRole);
    }


    @Test
    void testEmptyConstructor() {
        AppUser user = new AppUser();
        assertThat(user.getId()).isEqualTo(0L);
        assertThat(user.getNetId()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isNull();
    }

    @Test
    void testConstructor() {
        assertThat(user.getId()).isEqualTo(0);
        assertThat(user.getNetId()).isEqualTo(testNetID);
        assertThat(user.getPassword()).isEqualTo(testPassword);
        assertThat(user.getRole()).isEqualTo(testRole);
    }

    @Test
    void setNetId() {
        assertThat(user.getNetId()).isEqualTo(testNetID);
        NetId newNetID = new NetId("newUser");
        user.setNetId(newNetID);
        assertThat(user.getNetId()).isEqualTo(newNetID);
    }

    @Test
    void setPassword() {
        assertThat(user.getPassword()).isEqualTo(testPassword);
        HashedPassword newPassword = new HashedPassword("newpassword123");
        user.setPassword(newPassword);
        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void setRole() {
        assertThat(user.getRole()).isEqualTo(testRole);
        Role newRole = Role.FACULTY_REVIEWER;
        user.setRole(newRole);
        assertThat(user.getRole()).isEqualTo(newRole);
    }

    @Test
    void testToString() {
        String userDetails = "AppUser{" +
                "NetId = SomeUser" +
                ", password = password123" +
                ", role = EMPLOYEE" +
                '}';
        assertThat(user.toString()).isEqualTo(userDetails);
    }

    @Test
    void testEquals() {
        AppUser newUser = new AppUser(testNetID, testPassword, testRole);
        newUser.setId(0);
        assertThat(user).isEqualTo(user);
        assertThat(newUser).isEqualTo(user);
    }

    @Test
    void testNotEquals1() {
        Role newRole = Role.SYSADMIN;
        AppUser newUser = new AppUser(testNetID, testPassword, newRole);
        newUser.setId(1);
        assertThat(newUser).isNotEqualTo(user);
    }

    @Test
    void testEqualsHashCode() {
        AppUser newUser = new AppUser(testNetID, testPassword, testRole);
        newUser.setId(0);
        assertThat(newUser.hashCode()).isEqualTo(user.hashCode());
    }

}