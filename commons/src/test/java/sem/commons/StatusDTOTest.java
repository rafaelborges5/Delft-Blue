package sem.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusDTOTest {

    StatusDTO statusDTO;

    @BeforeEach
    void setUp() {
        statusDTO = new StatusDTO("OK");
    }

    @Test
    void notNullConstructor() {
        assertNotNull(statusDTO);
    }

    @Test
    void getStatus() {
        assertEquals("OK", statusDTO.getStatus());
    }

    @Test
    void setStatus() {
        statusDTO.setStatus("NOT OK");
        assertEquals("NOT OK", statusDTO.getStatus());
    }

    @Test
    void testEquals() {
        StatusDTO statusDTO2 = new StatusDTO("OK");
        assertEquals(statusDTO2, statusDTO);
    }
}