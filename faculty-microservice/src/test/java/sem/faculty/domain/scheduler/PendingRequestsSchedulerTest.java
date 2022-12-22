package sem.faculty.domain.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import sem.commons.FacultyName;
import sem.commons.NotificationDTO;
import sem.commons.Resource;
import sem.commons.NotValidResourcesException;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.*;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PendingRequestsSchedulerTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    @Mock
    private final RequestRepository requestRepository = mock(RequestRepository.class);
    @Mock
    private ScheduleRequestController controller;
    SchedulableRequestsScheduler scheduler;

    KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    @BeforeEach
    void setUp() {
        controller = mock(ScheduleRequestController.class);
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        scheduler = new PendingRequestsScheduler(controller, requestRepository, kafkaTemplate);
    }

    @Test
    void saveRequestInFaculty() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = mock(Faculty.class);

        scheduler.saveRequestInFaculty(request, faculty, date);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
        verify(faculty, times(1)).addPendingRequest(request);
        verifyNoMoreInteractions(faculty);
    }

    // Test abstract parent class
    @Test
    void schedulePendingRequest() throws NotValidResourcesException, ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);

        when(controller.sendScheduleRequest(any())).thenReturn(ResponseEntity.ok(date));

        scheduler.scheduleRequest(request, faculty);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
    }

    @Test
    void scheduleResponseNullRequest() throws NotValidResourcesException, ExecutionException, InterruptedException {
        LocalDate date = null;
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);
        when(controller.sendScheduleRequest(any())).thenReturn(ResponseEntity.ok(null));
        scheduler.scheduleRequest(request, faculty);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.DENIED);
    }

    @Test
    void getAvailableDate()
            throws NotValidResourcesException, NotEnoughResourcesLeftException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);
        when(controller.sendScheduleRequest(any())).thenReturn(ResponseEntity.ok(date));

        LocalDate availableDate = scheduler.getAvailableDate(request, faculty.getFacultyName());
        assertThat(availableDate).isEqualTo(date);
    }

    @Test
    void getAvailableDateThrowsError()
            throws NotValidResourcesException, NotEnoughResourcesLeftException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);
        when(controller.sendScheduleRequest(any())).thenReturn(ResponseEntity.ok(null));

        assertThatThrownBy(() -> {
            scheduler.getAvailableDate(request, faculty.getFacultyName());
        }).isInstanceOf(NotEnoughResourcesLeftException.class);
    }

    @Test
    void scheduleExecutionException()
            throws NotValidResourcesException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = mock(Faculty.class);
        when(controller.sendScheduleRequest(any())).thenThrow(ExecutionException.class);

        assertThatThrownBy(() -> {
            scheduler.scheduleRequest(request, faculty);
        }).isInstanceOf(RuntimeException.class);
        verify(faculty, times(1)).getFacultyName();
        verifyNoMoreInteractions(faculty);
    }

    @Test
    void scheduleInteruptedException()
            throws NotValidResourcesException,
            ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = mock(Faculty.class);
        when(controller.sendScheduleRequest(any())).thenThrow(InterruptedException.class);

        assertThatThrownBy(() -> {
            scheduler.scheduleRequest(request, faculty);
        }).isInstanceOf(RuntimeException.class);
        verify(faculty, times(1)).getFacultyName();
        verifyNoMoreInteractions(faculty);
    }
}