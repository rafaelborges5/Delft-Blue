package sem.faculty.handler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import sem.commons.*;
import sem.commons.NotValidResourcesException;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.*;
import sem.faculty.domain.scheduler.AcceptRequestsScheduler;
import sem.faculty.domain.scheduler.DenyRequestsScheduler;
import sem.faculty.domain.scheduler.PendingRequestsScheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FacultyHandlerTest {

    FacultyHandler facultyHandler;
    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    @Mock
    private final ScheduleRequestController scheduleRequestController = mock(ScheduleRequestController.class);
    @Mock
    private final RequestRepository requestRepository = mock(RequestRepository.class);

    @Mock
    private transient KafkaTemplate<String, NotificationDTO> kafkaTemplate = mock(KafkaTemplate.class);


    @BeforeEach
    void setUp() {
        facultyHandler = new FacultyHandler(kafkaTemplate);
        facultyHandler.timeProvider = timeProvider;
        facultyHandler.requestRepository = requestRepository;
        facultyHandler.scheduleRequestController = scheduleRequestController;
    }

    @Test
    void newFacultyHandler() {
        FacultyHandler facultyHandler1 = new FacultyHandler(kafkaTemplate);
        Map<FacultyName, Faculty> faculties = facultyHandler1.faculties;

        for (FacultyName fn : FacultyName.values()) {
            assertNotNull(faculties.get(fn));
        }
    }

    @Test
    void handleIncomingRequestsDenyScheduler() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 15);
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 14);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        when(timeProvider.getCurrentDate()).thenReturn(today);
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(DenyRequestsScheduler.class);
    }

    @Test

    void handleIncomingRequestsPendingScheduler()
            throws NotValidResourcesException, ExecutionException, InterruptedException {
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 14);
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 14, 12, 0);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 15);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        facultyHandler.requestRepository.save(request);
        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        when(scheduleRequestController.sendScheduleRequest(any()))
                .thenReturn(ResponseEntity.ok(todayDate));
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(PendingRequestsScheduler.class);
    }

    @Test
    void getPendingRequests() {
        Faculty faculty = new Faculty(FacultyName.EEMCS, new CurrentTimeProvider());
        facultyHandler.faculties.put(FacultyName.EEMCS, faculty);

        assertEquals(facultyHandler.getPendingRequests(FacultyName.EEMCS), new ArrayList<>());
    }

    //Tests for not accepting anymore 5 minutes before the preferred day starts.
    @Test
    void handleIncomingRequestsTwoMinutesBeforePreferredDate() throws NotValidResourcesException {
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 12, 23, 58);
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 12);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 13);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(DenyRequestsScheduler.class);
    }

    @Test
    void handleIncomingRequestsFiveMinutesBeforePreferredDate() throws NotValidResourcesException {
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 12, 23, 55);
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 12);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 13);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(DenyRequestsScheduler.class);
    }

    //Tests for accepting requests within 6 hours before the preferred day
    @Test
    void handleIncomingRequestsSixHoursBeforePreferredDate() throws NotValidResourcesException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 13); //Preferred LocalDate for the request
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 12);
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 12, 18, 0);

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        when(scheduleRequestController.sendScheduleRequest(any()))
                .thenReturn(ResponseEntity.ok(todayDate));
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(AcceptRequestsScheduler.class);
    }

    @Test
    void handleIncomingRequestsSevenHoursBeforePreferredDate() throws NotValidResourcesException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 13); //Preferred LocalDate for the request
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 12);
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 12, 17, 59);

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        when(scheduleRequestController.sendScheduleRequest(any()))
                .thenReturn(ResponseEntity.ok(todayDate));

        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(PendingRequestsScheduler.class);
    }

    @Test
    void handleIncomingRequestsOneHourBeforePreferredDate() throws NotValidResourcesException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 13); //Preferred LocalDate for the request
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 12);
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 12,
                23, 54, 59);

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        when(scheduleRequestController.sendScheduleRequest(any()))
                .thenReturn(ResponseEntity.ok(todayDate));
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(AcceptRequestsScheduler.class);
    }

    @Test
    void getRequestForDate() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2015, 2, 3);
        Request request = new Request("name", "netId", "desc", LocalDate.of(2015, 2, 4),
                RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        RequestDTO requestDTO = new RequestDTO(request.getRequestId(), request.getName(),
                request.getNetId(), request.getFacultyName(), request.getDescription(), request.getPreferredDate(),
                request.getResource());
        facultyHandler.faculties.get(FacultyName.EEMCS).getSchedule().put(date, List.of(request));
        Map<FacultyName, List<RequestDTO>> map = facultyHandler.getRequestForDate(date);
        assertEquals(map.get(FacultyName.EEMCS).get(0), requestDTO);
    }
}