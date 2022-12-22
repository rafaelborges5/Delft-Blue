package sem.faculty.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import org.mockito.Mockito;
import sem.commons.FacultyName;
import sem.commons.Resource;
import sem.commons.NotValidResourcesException;
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
    void scheduleDate_creates_new_list() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        Map<LocalDate, List<Long>> expected = new HashMap<>();
        expected.put(date, List.of(request1.getRequestId()));

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

        Map<LocalDate, List<Long>> expected = new HashMap<>();
        expected.put(date, List.of(request1.getRequestId(), request2.getRequestId()));

        faculty.scheduleForDate(request1, date);
        faculty.scheduleForDate(request2, date);
        assertThat(faculty.getSchedule()).isEqualTo(expected);
    }

    @Test
    void addPendingRequest() throws NotValidResourcesException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.PENDING, FacultyName.EEMCS, new Resource(1, 1, 1));

        assertThat(faculty.getPendingRequests()).isEmpty();
        faculty.addPendingRequest(request1);
        List<Long> queue = faculty.getPendingRequests();
        assertThat(queue.size()).isEqualTo(1);
        assertThat(queue.get(0)).isEqualTo(request1.getRequestId());
    }
}
