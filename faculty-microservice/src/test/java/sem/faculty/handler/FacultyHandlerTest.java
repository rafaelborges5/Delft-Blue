package sem.faculty.handler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.*;
import sem.faculty.domain.scheduler.DenyRequestsScheduler;
import sem.faculty.domain.scheduler.PendingRequestsScheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;
import sem.commons.FacultyName;
import sem.commons.Resource;
import sem.commons.NotValidResourcesException;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FacultyHandlerTest {

    @Mock
    private final TimeProvider timeProvider = mock(CurrentTimeProvider.class);
    @Mock
    private final ScheduleRequestController scheduleRequestController = mock(ScheduleRequestController.class);

    FacultyHandler facultyHandler;


    @BeforeEach
    void setUp() {
        facultyHandler = new FacultyHandler();
        facultyHandler.timeProvider = timeProvider;
        facultyHandler.scheduleRequestController = scheduleRequestController;
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
        when(timeProvider.getCurrentTime()).thenReturn(today);
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(DenyRequestsScheduler.class);
    }

    @Test

    void handleIncomingRequestsPendingScheduler()
            throws NotValidResourcesException, ExecutionException, InterruptedException {
        LocalDate date = LocalDate.of(2022, Month.DECEMBER, 15);
        Request request = new Request("Name1", "NetID", "Desription",
                date, RequestStatus.ACCEPTED, FacultyName.EEMCS, new Resource(1, 1, 1));
        when(timeProvider.getCurrentTime()).thenReturn(date);
        when(scheduleRequestController.sendScheduleRequest(any()))
                .thenReturn(ResponseEntity.ok(date.plusDays(1)));
        facultyHandler.handleIncomingRequests(request);
        assertThat(facultyHandler.scheduler.getClass()).isEqualTo(PendingRequestsScheduler.class);
    }

    @Test
    void getPendingRequests() {
        Faculty faculty = new Faculty(FacultyName.EEMCS, new CurrentTimeProvider());
        facultyHandler.faculties.put(FacultyName.EEMCS, faculty);

        assertEquals(facultyHandler.getPendingRequests(FacultyName.EEMCS), new ArrayList<>());
    }

}