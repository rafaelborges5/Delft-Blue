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

    @BeforeEach
    void setUp() {
        list = List.of(new RequestDTO("netId", "name", "desc", LocalDate.of(2015, 2, 3), 5, 2, 1));
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
    void setAcceptedRequests() {
        List<RequestDTO> list2 = List.of(
                new RequestDTO("netId2", "name2", "desc2", LocalDate.of(2015, 2, 3), 5, 2, 1)
        );
        acceptRequestsDTO.setAcceptedRequests(list2);
        assertEquals(list2, acceptRequestsDTO.getAcceptedRequests());
    }

    @Test
    void testEquals() {
        List<RequestDTO> list2 = List.of(
                new RequestDTO("netId", "name", "desc", LocalDate.of(2015, 2, 3), 5, 2, 1)
        );
        AcceptRequestsDTO acceptRequestsDTO2 = new AcceptRequestsDTO("EEMCS", list2);
        assertEquals(acceptRequestsDTO, acceptRequestsDTO2);
    }
}