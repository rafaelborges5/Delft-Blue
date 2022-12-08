package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import org.mockito.Mockito;
import provider.CurrentTimeProvider;
import provider.TimeProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

class SchedulerTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    private Scheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new Scheduler(timeProvider);
    }

    @Test
    void testScheduleRequestException() {
        Request requestError = new Request("Name", "NetID", "Desription",
                LocalDate.of(2022, Month.DECEMBER, 7),
                RequestStatus.DROPPED, new Resource(1, 1, 1));
        assertThrows(RejectRequestException.class, () -> {
            scheduler.scheduleRequest(requestError);
        });
    }

    @Test
    void testScheduleNotEnoughResourcesLeftException() {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 5);
        LocalDate preferredDate = LocalDate.of(2022, Month.DECEMBER, 5);

        when(timeProvider.getCurrentTime()).thenReturn(today);

        Request request1 = new Request("Name1", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        Request request2 = new Request("Name2", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        Request request3 = new Request("Name3", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        assertThrows(NotEnoughResourcesLeftException.class, () -> {
            scheduler.scheduleRequest(request1);
            scheduler.scheduleRequest(request2);
            scheduler.scheduleRequest(request3);
        });
    }

    @Test
    void testScheduleRequest() {
        LocalDate today = LocalDate.of(2022, Month.DECEMBER, 5);
        LocalDate preferredDate = LocalDate.of(2022, Month.DECEMBER, 7);

        when(timeProvider.getCurrentTime()).thenReturn(today);

        Request request1 = new Request("Name1", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        Request request2 = new Request("Name2", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        Request request3 = new Request("Name3", "NetID", "Desription",
                preferredDate, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        try {
            scheduler.scheduleRequest(request1);
            scheduler.scheduleRequest(request2);
            scheduler.scheduleRequest(request3);

        } catch (NotEnoughResourcesLeftException | RejectRequestException e) {
            throw new RuntimeException(e);
        }

        Mockito.verify(timeProvider, times(3)).getCurrentTime();

        Map<LocalDate, List<Request>> expected = new HashMap<>();
        expected.put(preferredDate, List.of(request1, request2));
        expected.put(preferredDate.minusDays(1), List.of(request3));

        assertThat(scheduler.getSchedule()).isEqualTo(expected);
    }

    @Test
    void scheduleDate_creates_new_list() {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        Map<LocalDate, List<Request>> expected = new HashMap<>();
        expected.put(date, List.of(request1));

        scheduler.scheduleForDate(request1, date);
        assertThat(scheduler.getSchedule()).isEqualTo(expected);
    }

    @Test
    void scheduleDate_add_to_list() {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, new Resource(1, 1, 1));
        Request request2 = new Request("Name2", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, new Resource(1, 1, 1));

        Map<LocalDate, List<Request>> expected = new HashMap<>();
        expected.put(date, List.of(request1, request2));

        scheduler.scheduleForDate(request1, date);
        scheduler.scheduleForDate(request2, date);
        assertThat(scheduler.getSchedule()).isEqualTo(expected);
    }

    //TODO: The tests for canScheduleForDate might need to be improved after the final version is implemented.
    @Test
    void canScheduleForDate() {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 7);
        Request request1 = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, new Resource(1, 1, 1));
        assertThat(scheduler.canScheduleForDate(request1, date)).isTrue();
    }
}
