package sem.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sem.commons.FacultyName;
import sem.commons.NotValidResourcesException;
import sem.commons.RequestDTO;
import sem.commons.Resource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


class RequestDTOTest {
    long id;
    RequestDTO requestDTO;
    FacultyName faculty;
    String name;
    String netId;
    String descr;
    LocalDate date;
    Resource resource;

    @BeforeEach
    void createRequestDTO() throws NotValidResourcesException {
        id = 0L;
        name = "Name";
        netId = "NETID";
        faculty = FacultyName.EEMCS;
        descr = "Description";
        date = LocalDate.of(2022, 12, 5);
        resource = new Resource(1, 1, 1);
        requestDTO = new RequestDTO(id, name, netId, faculty, descr, date, resource);
    }

    @Test
    void testEmptyConstructor() {
        RequestDTO empty = new RequestDTO();
        assertThat(empty.getRequestId()).isEqualTo(0L);
        assertThat(empty.getNetId()).isNull();
        assertThat(empty.getName()).isNull();
        assertThat(empty.getFaculty()).isNull();
        assertThat(empty.getDescription()).isNull();
        assertThat(empty.getPreferredDate()).isNull();
        assertThat(empty.getResource()).isNull();
    }

    @Test
    void testConstructor() {
        assertThat(requestDTO.getRequestId()).isEqualTo(0L);
        assertThat(requestDTO.getName()).isEqualTo(name);
        assertThat(requestDTO.getNetId()).isEqualTo(netId);
        assertThat(requestDTO.getFaculty()).isEqualTo(faculty);
        assertThat(requestDTO.getDescription()).isEqualTo(descr);
        assertThat(requestDTO.getPreferredDate()).isEqualTo(date);
        assertThat(requestDTO.getResource()).isEqualTo(resource);
    }

    @Test
    void getRequestId() {
        assertThat(requestDTO.getRequestId()).isEqualTo(0L);
    }

    @Test
    void getNetId() {
        assertThat(requestDTO.getNetId()).isEqualTo(netId);
    }

    @Test
    void getName() {
        assertThat(requestDTO.getName()).isEqualTo(name);
    }

    @Test
    void getFaculty() {
        assertThat(requestDTO.getFaculty()).isEqualTo(faculty);
    }

    @Test
    void getDescription() {
        assertThat(requestDTO.getDescription()).isEqualTo(descr);
    }

    @Test
    void getPreferredDate() {
        assertThat(requestDTO.getPreferredDate()).isEqualTo(date);
    }

    @Test
    void getResource() {
        assertThat(requestDTO.getResource()).isEqualTo(resource);
    }

    @Test
    void setNetId() {
        assertThat(requestDTO.getNetId()).isEqualTo(netId);
        requestDTO.setNetId("NEW NETID");
        assertThat(requestDTO.getNetId()).isEqualTo("NEW NETID");
    }

    @Test
    void setName() {
        assertThat(requestDTO.getName()).isEqualTo(name);
        requestDTO.setName("NEW NAME");
        assertThat(requestDTO.getName()).isEqualTo("NEW NAME");
    }

    @Test
    void setDescription() {
        assertThat(requestDTO.getDescription()).isEqualTo(descr);
        requestDTO.setDescription("NEW DESCRIPTION");
        assertThat(requestDTO.getDescription()).isEqualTo("NEW DESCRIPTION");
    }

    @Test
    void setPreferredDate() {
        assertThat(requestDTO.getPreferredDate()).isEqualTo(date);
        LocalDate newDate = LocalDate.of(2022, 12, 6);
        requestDTO.setPreferredDate(newDate);
        assertThat(requestDTO.getPreferredDate()).isEqualTo(newDate);
    }
}
