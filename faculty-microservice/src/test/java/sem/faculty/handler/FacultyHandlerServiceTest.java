package sem.faculty.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sem.commons.*;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FacultyHandlerServiceTest {

    private FacultyHandler facultyHandler;
    private FacultyHandlerService facultyHandlerService;
    @Mock
    private RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        facultyHandler = Mockito.mock(FacultyHandler.class);
        requestRepository = Mockito.mock(RequestRepository.class);
        facultyHandlerService = new FacultyHandlerService(facultyHandler, requestRepository);
    }

    @Test
    void getPendingRequestsCorrect() throws NotValidResourcesException {
        Request req = new Request("name", "netId", "desc",
                        LocalDate.of(2015, 2, 3),
                        RequestStatus.PENDING, FacultyName.EEMCS, new Resource(1, 1, 1));
        List<Long> list = List.of(req.getRequestId());
        when(facultyHandler.getPendingRequests(FacultyName.EEMCS)).thenReturn(list);
        PendingRequestsDTO pendingRequestsDTO = facultyHandlerService.getPendingRequests("EEMCS");
        assertEquals("OK", pendingRequestsDTO.getStatus());
        RequestDTO requestDTO = pendingRequestsDTO.getRequests().get(0);
        assertEquals("name", requestDTO.getName());
        assertEquals(FacultyName.EEMCS, requestDTO.getFaculty());
        assertEquals("netId", requestDTO.getNetId());
    }

    @Test
    void getPendingRequestsWrong() throws NotValidResourcesException {

        PendingRequestsDTO pendingRequestsDTO = facultyHandlerService.getPendingRequests("FOO");
        assertEquals("Wrong faculty name", pendingRequestsDTO.getStatus());
        assertEquals(new ArrayList<RequestDTO>(), pendingRequestsDTO.getRequests());
    }

    @Test
    void acceptRequestsCorrect() {
        StatusDTO statusDTO = facultyHandlerService.acceptRequests("EEMCS", List.of(1L));
        assertEquals("OK", statusDTO.getStatus());
    }

    @Test
    void acceptRequestsWrong() {
        StatusDTO statusDTO = facultyHandlerService.acceptRequests("FOO", List.of(1L));
        assertEquals("Wrong faculty name", statusDTO.getStatus());
    }
}