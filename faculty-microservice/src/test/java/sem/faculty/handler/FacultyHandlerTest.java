package sem.faculty.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sem.commons.FacultyName;
import sem.faculty.domain.Faculty;
import sem.faculty.provider.CurrentTimeProvider;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FacultyHandlerTest {

    FacultyHandler facultyHandler;

    @BeforeEach
    void setUp() {
        facultyHandler = new FacultyHandler();
    }

    @Test
    void newFacultyHandler() {
        FacultyHandler facultyHandler1 = facultyHandler.newFacultyHandler();
        Map<FacultyName, Faculty> faculties = facultyHandler1.faculties;

        for (FacultyName fn : FacultyName.values()) {
            assertNotNull(faculties.get(fn));
        }
    }

    @Test
    void getPendingRequests() {
        Faculty faculty = new Faculty(FacultyName.EEMCS, new CurrentTimeProvider());
        facultyHandler.faculties.put(FacultyName.EEMCS, faculty);

        assertEquals(facultyHandler.getPendingRequests(FacultyName.EEMCS), new ArrayList<>());
    }


}