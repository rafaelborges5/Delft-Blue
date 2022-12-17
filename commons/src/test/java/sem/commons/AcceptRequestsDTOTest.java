package sem.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcceptRequestsDTOTest {

    AcceptRequestsDTO acceptRequestsDTO;
    List<RequestDTO> list;
    Resource resource;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        resource = new Resource(3, 2, 1);
        list = List.of(new RequestDTO(0L, "name", "NETID", FacultyName.EEMCS, "descr",
                LocalDate.of(2015, 2, 3), resource));
        acceptRequestsDTO = new AcceptRequestsDTO("EEMCS", list);
    }

    @Test
    void notNull() {
        assertNotNull(acceptRequestsDTO);
    }

    @Test
    void getFacultyName() {
        assertEquals("EEMCS", acceptRequestsDTO.getFacultyName());
    }

    @Test
    void getAcceptedRequests() {
        assertEquals(list, acceptRequestsDTO.getAcceptedRequests());
    }

    @Test
    void setFacultyName() {
        acceptRequestsDTO.setFacultyName("AE");
        assertEquals("AE", acceptRequestsDTO.getFacultyName());
    }

    @Test
    void setAcceptedRequests() throws NotValidResourcesException {
        List<RequestDTO> list2 = List.of(
               new RequestDTO(0L, "name2", "NETID2", FacultyName.EEMCS, "descr2",
                        LocalDate.of(2015, 2, 3), resource)
        );
        acceptRequestsDTO.setAcceptedRequests(list2);
        assertEquals(list2, acceptRequestsDTO.getAcceptedRequests());
    }

    @Test
    void testEquals() throws NotValidResourcesException {
        List<RequestDTO> list2 = List.of(
                new RequestDTO(0L, "name", "NETID", FacultyName.EEMCS, "descr",
                LocalDate.of(2015, 2, 3), resource)
        );
        AcceptRequestsDTO acceptRequestsDTO2 = new AcceptRequestsDTO("EEMCS", list2);
        assertEquals(acceptRequestsDTO2, acceptRequestsDTO);
    }
}