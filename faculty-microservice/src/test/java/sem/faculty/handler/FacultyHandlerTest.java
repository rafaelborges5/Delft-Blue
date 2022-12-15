package sem.faculty.handler;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import sem.faculty.domain.*;
import sem.faculty.domain.scheduler.DenyScheduler;
import sem.faculty.domain.scheduler.PendingScheduler;
import sem.faculty.domain.scheduler.Scheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FacultyHandlerTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    FacultyHandler facultyHandler = new FacultyHandler(timeProvider);
    @Test
    void populateFaculties() {
        for(FacultyName fn: FacultyName.values()) {
            assertThat(facultyHandler.faculties.get(fn)).isNotNull();
        }
    }

    @Test
    void handleIncomingRequestsDenyScheduler() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 15);
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 14);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        when(timeProvider.getCurrentTime()).thenReturn(today);
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(DenyScheduler.class);
    }
    @Test
    void handleIncomingRequestsPendingScheduler() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 15);
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 15);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        when(timeProvider.getCurrentTime()).thenReturn(today);
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(PendingScheduler.class);
    }
}