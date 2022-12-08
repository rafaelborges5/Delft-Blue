package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Service;
import provider.TimeProvider;

@Service
@Getter
public class Scheduler {

    //TODO: Replace ALLOCATED_RESOURCES with an actual bound after resource_manager is implemented.
    private static final long ALLOCATED_RESOURCES = 2;

    private Map<LocalDate, List<Request>> schedule;
    private final TimeProvider timeProvider;

    public Scheduler(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.schedule = new HashMap<>();
    }

    /**
     * Schedule a request based on the preferred date.
     *
     * @param request - Request that will be scheduled.
     * @throws RejectRequestException - if the request was not approved.
     */
    public void scheduleRequest(Request request) throws RejectRequestException, NotEnoughResourcesLeftException {
        if (!request.getStatus().equals(RequestStatus.ACCEPTED)) {
            throw new RejectRequestException(request.getRequestId());
        }

        LocalDate currentDate = timeProvider.getCurrentTime();
        LocalDate scheduledDate = request.getPreferredDate();
        while (!scheduledDate.isBefore(currentDate)) {
            if (canScheduleForDate(request, scheduledDate)) {
                scheduleForDate(request, scheduledDate);
                return;
            }
            scheduledDate = scheduledDate.minusDays(1);
        }
        throw new NotEnoughResourcesLeftException(request.getRequestId());
    }

    /**
     * Check if a request can be scheduled on a given day.
     *
     * @param request       - Request that will be scheduled.
     * @param scheduledDate - Date on which to check if the request can be run.
     * @return true iff the request can be run on the given date.
     */
    protected boolean canScheduleForDate(Request request, LocalDate scheduledDate) {
        List<Request> list = schedule.get(scheduledDate);

        /*
          TODO: Replace this after Resource class is implemented
          For now, the number of request is used as a metric.
          TODO: Replace ALLOCATED_RESOURCES with an actual bound after resource_manager is implemented.
          For now, this is checked against a hardcoded value.
         */
        long resources = (list == null) ? 0 : list.size();
        return resources + 1 <= ALLOCATED_RESOURCES;
    }

    /**
     * This is extracted for easier additions and changes in the future.
     * It updates the map with the request on the specified scheduledDate.
     *
     * @param request       - Request that is added to schedule.
     * @param scheduledDate - LocalDate on which the request is scheduled.
     */
    protected void scheduleForDate(Request request, LocalDate scheduledDate) {
        List<Request> list = schedule.get(scheduledDate);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(request);
        schedule.put(scheduledDate, list);
    }
}
