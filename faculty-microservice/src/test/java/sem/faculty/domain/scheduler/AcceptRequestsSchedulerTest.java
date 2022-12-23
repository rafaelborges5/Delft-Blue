package sem.faculty.domain.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import sem.commons.*;
import sem.commons.NotValidResourcesException;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.*;
import sem.faculty.handler.FacultyHandler;
import sem.faculty.handler.FacultyHandlerService;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AcceptRequestsSchedulerTest {

    SchedulableRequestsScheduler scheduler;
    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    @Mock
    private final ScheduleRequestController controller = mock(ScheduleRequestController.class);

    @Mock
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate = mock(KafkaTemplate.class);

    @Mock
    private final RequestRepository requestRepository = mock(RequestRepository.class);


    @BeforeEach
    void setUp() {
        scheduler = new AcceptRequestsScheduler(controller, requestRepository, kafkaTemplate);
    }

    @Test
    void saveRequestInFaculty() throws NotValidResourcesException, ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = mock(Faculty.class);
        when(controller.sendReserveResources(any())).thenReturn(new StatusDTO("OK"));

        scheduler.saveRequestInFaculty(request, faculty, date);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.ACCEPTED);
        verify(faculty).getFacultyName();
        verify(faculty, times(1)).scheduleForDate(request, date);
        verifyNoMoreInteractions(faculty);
    }
}