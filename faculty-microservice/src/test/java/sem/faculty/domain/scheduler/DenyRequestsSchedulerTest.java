package sem.faculty.domain.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import sem.faculty.domain.*;
import sem.faculty.provider.CurrentTimeProvider;

import java.time.LocalDate;
import java.time.Month;

class DenyRequestsSchedulerTest {
    DenyRequestsScheduler denyScheduler = new DenyRequestsScheduler();

    @Test
    void testScheduleRequest() throws NotValidResourcesException {
        Request request = new Request("name", "netId", "description",
                LocalDate.of(2022, Month.DECEMBER, 15), RequestStatus.DROPPED,
                FacultyName.ARCH, new Resource(5, 1, 1));

        denyScheduler.scheduleRequest(request,
                new Faculty(FacultyName.ARCH, new CurrentTimeProvider()));

        assertThat(request.getStatus()).isEqualTo(RequestStatus.DENIED);
    }
}
