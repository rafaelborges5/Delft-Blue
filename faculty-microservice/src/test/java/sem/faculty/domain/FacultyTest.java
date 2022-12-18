package sem.faculty.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import org.mockito.Mockito;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

class FacultyTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty(FacultyName.EEMCS, timeProvider);
    }

    @Test
    void testScheduleRequestException() throws NotValidResourcesException {
        Request requestError = new Request("Name", "NetID", "Desription",
                LocalDate.of(2022, Month.DECEMBER, 7),
                RequestStatus.DROPPED, FacultyName.EEMCS, new Resource(1, 1, 1));
        assertThrows(RejectRequestException.class, () -> {
            faculty.scheduleRequest(requestError);
        });
    }

    @Test
    void testScheduleNotEnoughResourcesLeftException() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 5);
        LocalDate preferredDate = LocalDate.of(2022, Month.DECEMBER, 5);

        when(timeProvider.getCurrentTime()).thenReturn(today);

        Request request1 = new Request("Name1", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Request request2 = new Request("Name2", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Request request3 = new Request("Name3", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        assertThrows(NotEnoughResourcesLeftException.class, () -> {
            faculty.scheduleRequest(request1);
            faculty.scheduleRequest(request2);
            faculty.scheduleRequest(request3);
        });
    }

    @Test
    void testScheduleRequest() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 5);
        LocalDate preferredDate = LocalDate.of(2022, Month.DECEMBER, 7);

        when(timeProvider.getCurrentTime()).thenReturn(today);

        Request request1 = new Request("Name1", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Request request2 = new Request("Name2", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Request request3 = new Request("Name3", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        try {
            faculty.scheduleRequest(request1);
            faculty.scheduleRequest(request2);
            faculty.scheduleRequest(request3);

        } catch (NotEnoughResourcesLeftException | RejectRequestException e) {
            throw new RuntimeException(e);
        }

        Mockito.verify(timeProvider, times(3)).getCurrentTime();

        Map<LocalDate, List<Request>> expected = new HashMap<>();
        expected.put(preferredDate, List.of(request1, request2));
        expected.put(preferredDate.minusDays(1), List.of(request3));

        assertThat(faculty.getSchedule()).isEqualTo(expected);
    }

    @Test
    void scheduleDate_creates_new_list() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Map<LocalDate, List<Request>> expected = new HashMap<>();
        expected.put(date, List.of(request1));

        faculty.scheduleForDate(request1, date);
        assertThat(faculty.getSchedule()).isEqualTo(expected);
    }

    @Test
    void scheduleDate_add_to_list() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        Request request2 = new Request("Name2", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Map<LocalDate, List<Request>> expected = new HashMap<>();
        expected.put(date, List.of(request1, request2));

        faculty.scheduleForDate(request1, date);
        faculty.scheduleForDate(request2, date);
        assertThat(faculty.getSchedule()).isEqualTo(expected);
    }

    /*@Test
    void testDeniedHandleIncomingRequest() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 8);
        when(timeProvider.getCurrentTime()).thenReturn(today);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.DROPPED, FacultyName.EEMCS, new Resource(1, 1, 1));

        faculty.handleIncomingRequest(request1);
        assertThat(request1.getStatus()).isEqualTo(RequestStatus.DENIED);
    }

    @Test
    void testPendingHandleIncomingRequest() throws NotValidResourcesException {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 6);
        when(timeProvider.getCurrentTime()).thenReturn(today);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.DROPPED, FacultyName.EEMCS, new Resource(1, 1, 1));

        faculty.handleIncomingRequest(request1);
        assertThat(request1.getStatus()).isEqualTo(RequestStatus.PENDING);
        assertThat(faculty.getPendingRequests().contains(request1));
    }*/

    //TODO: The tests for canScheduleForDate might need to be improved after the final version is implemented.
    @Test
    void canScheduleForDate() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        assertThat(faculty.checkAvailabilityForDate(request1, date)).isTrue();
    }

    @Test
    void addPendingRequest() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.PENDING, FacultyName.EEMCS, new Resource(1, 1, 1));

        assertThat(faculty.getPendingRequests()).isEmpty();
        faculty.addPendingRequest(request1);
        Queue<Request> queue = faculty.getPendingRequests();
        assertThat(queue.size()).isEqualTo(1);
        assertThat(queue.poll()).isEqualTo(request1);
    }
}
