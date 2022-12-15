package sem.faculty.domain.scheduler;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.parameters.P;
import sem.faculty.domain.*;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PendingSchedulerTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    PendingScheduler scheduler = new PendingScheduler();
    @Test
    void schedulePendingRequest() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);

        scheduler.scheduleRequest(request, faculty);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
    }
    @Test
    void scheduleDeniedRequest() throws NotValidResourcesException {
        LocalDate date = null;
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);

        scheduler.scheduleRequest(request, faculty);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.DENIED);
    }

    @Test
    void getAvailableDate() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 17);
        Request request = new Request("name", "netId", "description",
                date, RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));
        Faculty faculty = new Faculty(FacultyName.ARCH, timeProvider);

        LocalDate availableDate = scheduler.getAvailableDate(request, faculty.getFacultyName());
        assertThat(availableDate).isEqualTo(date);
    }
}