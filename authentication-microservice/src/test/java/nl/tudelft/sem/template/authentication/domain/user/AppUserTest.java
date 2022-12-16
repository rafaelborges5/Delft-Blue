package nl.tudelft.sem.template.authentication.domain.user;

import org.assertj.core.api.FactoryBasedNavigableIterableAssert;
import org.hamcrest.beans.HasProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTest;
import scala.App;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {
    AppUser user;
    Integer testid;
    NetId testNetID;
    HashedPassword testPassword;
    Role testRole;
    List<Faculty> testFaculty;

    @BeforeEach
    void createUser() {
        testNetID = new NetId("SomeUser");
        testPassword = new HashedPassword("password123");
        testRole = Role.EMPLOYEE;
        testFaculty = new ArrayList<>();
        testFaculty.add(Faculty.EEMCS);
        testFaculty.add(Faculty.CEG);
        user = new AppUser(testNetID, testPassword, testRole, testFaculty);
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
    void getFaculty() {
        assertThat(user.getFaculty()).isEqualTo(testFaculty);
    }

    @Test
    void testEmptyConstructor() {
        AppUser user = new AppUser();
        assertThat(user.getId()).isEqualTo(0L);
        assertThat(user.getNetId()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isNull();
        assertThat(user.getFaculty()).isNull();
    }

    @Test
    void testConstructor() {
        assertThat(user.getId()).isEqualTo(0);
        assertThat(user.getNetId()).isEqualTo(testNetID);
        assertThat(user.getPassword()).isEqualTo(testPassword);
        assertThat(user.getRole()).isEqualTo(testRole);
        assertThat(user.getFaculty()).isEqualTo(testFaculty);
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
    void setFaculty() {
        assertThat(user.getFaculty()).isEqualTo(testFaculty);
        List<Faculty> newFaculty = new ArrayList<>();
        newFaculty.add(Faculty.ARCH);
        newFaculty.add(Faculty.MMME);
        user.setFaculty(newFaculty);
        assertThat(user.getFaculty()).isEqualTo(newFaculty);
    }

    @Test
    void testAddFaculty() {
        assertThat(user.getFaculty()).isEqualTo(testFaculty);
        user.addFaculty(Faculty.AE);
        testFaculty.add(Faculty.AE);
        assertThat(user.getFaculty()).isEqualTo(testFaculty);
    }

    @Test
    void testFacultyString() {
        String facultyDetails = "EEMCS, CEG";
        assertThat(user.facultyString()).isEqualTo(facultyDetails);
    }

    @Test
    void testToString() {
        String userDetails = "AppUser{" +
                "NetId = SomeUser" +
                ", password = password123" +
                ", role = EMPLOYEE" +
                ", faculties = EEMCS, CEG" +
                '}';
        assertThat(user.toString()).isEqualTo(userDetails);
    }

    @Test
    void testEquals() {
        AppUser newUser = new AppUser(testNetID, testPassword, testRole, testFaculty);
        newUser.setId(0);
        assertThat(user).isEqualTo(user);
        assertThat(newUser).isEqualTo(user);
    }

    @Test
    void testNotEquals1() {
        Role newRole = Role.SYSADMIN;
        List<Faculty> newFaculty = new ArrayList<>();
        newFaculty.add(Faculty.MMME);
        AppUser newUser = new AppUser(testNetID, testPassword, newRole, newFaculty);
        newUser.setId(1);
        assertThat(newUser).isNotEqualTo(user);
    }

    @Test
    void testEqualsHashCode() {
        AppUser newUser = new AppUser(testNetID, testPassword, testRole, testFaculty);
        newUser.setId(0);
        assertThat(newUser.hashCode()).isEqualTo(user.hashCode());
    }

}