package sem.faculty.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sem.commons.*;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Request> list = List.of(new Request("name", "netId", "desc",
                        LocalDate.of(2015, 2, 3),
                        RequestStatus.PENDING, FacultyName.EEMCS, new Resource(1, 1, 1)));
        when(facultyHandler.getPendingRequests(FacultyName.EEMCS)).thenReturn(list);
        PendingRequestsDTO pendingRequestsDTO = facultyHandlerService.getPendingRequests("EEMCS");
        assertEquals("OK", pendingRequestsDTO.getStatus());
        RequestDTO requestDTO = pendingRequestsDTO.getRequests().get(0);
        assertEquals("name", requestDTO.getRequestResourceManagerInformation().getName());
        assertEquals(FacultyName.EEMCS, requestDTO.getRequestFacultyInformation().getFaculty());
        assertEquals("netId", requestDTO.getRequestFacultyInformation().getNetId());
    }

    @Test
    void getPendingRequestsWrong() throws NotValidResourcesException {

        PendingRequestsDTO pendingRequestsDTO = facultyHandlerService.getPendingRequests("FOO");
        assertEquals("Wrong faculty name", pendingRequestsDTO.getStatus());
        assertEquals(new ArrayList<RequestDTO>(), pendingRequestsDTO.getRequests());
    }

    @Test
    void acceptRequestsCorrect() throws NotValidResourcesException {
        Request request = new Request("name", "netId", "desc",
                LocalDate.of(2015, 2, 3),
                RequestStatus.PENDING, FacultyName.EEMCS, new Resource(1, 1, 1));
        when(requestRepository.findByRequestId(1L)).thenReturn(request);
        StatusDTO statusDTO = facultyHandlerService.acceptRequests("EEMCS", List.of(1L));
        assertEquals("OK", statusDTO.getStatus());
    }

    @Test
    void acceptRequestsWrong() {
        StatusDTO statusDTO = facultyHandlerService.acceptRequests("FOO", List.of(1L));
        assertEquals("Wrong faculty name", statusDTO.getStatus());
    }

    @Test
    void getScheduleForDate() throws NotValidResourcesException {
        Map<FacultyName, List<RequestDTO>> map = new HashMap<>();
        RequestDTO requestDTO = new RequestDTO(1L, "name", "netId", FacultyName.EEMCS,
                "desc", LocalDate.of(2015, 2, 3),
                new Resource(1, 1, 1));
        map.put(FacultyName.EEMCS, List.of(requestDTO));
        LocalDate date = LocalDate.of(2015, 2, 3);
        when(facultyHandler.getRequestForDate(date)).thenReturn(map);
        SysadminScheduleDTO sysadminScheduleDTO = facultyHandlerService.getScheduleForDate(date);
        assertEquals(sysadminScheduleDTO.getSchedule(), map);
    }
}