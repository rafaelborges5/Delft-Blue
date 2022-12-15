package sem.faculty.handler;

import org.junit.jupiter.api.Test;
import sem.faculty.domain.FacultyName;
import sem.faculty.domain.Request;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FacultyHandlerTest {

    @Test
    void getPendingRequests() {
        FacultyHandler fh = new FacultyHandler();
        assertThat(fh.getPendingRequests(FacultyName.EEMCS)).isEqualTo(new ArrayList<Request>());
    }
}