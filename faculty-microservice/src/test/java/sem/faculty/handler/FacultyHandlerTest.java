package sem.faculty.handler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import sem.faculty.domain.*;
import sem.faculty.domain.scheduler.DenyRequestsScheduler;
import sem.faculty.domain.scheduler.PendingRequestsScheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;
import sem.commons.FacultyName;
import sem.commons.Resource;
import sem.commons.NotValidResourcesException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FacultyHandlerTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    FacultyHandler facultyHandler;


    @BeforeEach
    void setUp() {
        facultyHandler = new FacultyHandler();
        facultyHandler.timeProvider = timeProvider;
    }

    @Test
    void newFacultyHandler() {
        FacultyHandler facultyHandler1 = facultyHandler.newFacultyHandler();
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
    void handleIncomingRequestsPendingScheduler() throws NotValidResourcesException {
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 14);
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 14, 12, 0);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 15);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

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

    @Test
    void handleIncomingRequestsSixMinutesBeforePreferredDate() throws NotValidResourcesException {
        LocalDateTime todayDateTime = LocalDateTime.of(2022, Month.DECEMBER, 12, 23, 54, 59);
        LocalDate todayDate = LocalDate.of(2022, Month.DECEMBER, 12);

        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 13);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));

        when(timeProvider.getCurrentDate()).thenReturn(todayDate);
        when(timeProvider.getCurrentDateTime()).thenReturn(todayDateTime);

        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(PendingRequestsScheduler.class);
    }

}