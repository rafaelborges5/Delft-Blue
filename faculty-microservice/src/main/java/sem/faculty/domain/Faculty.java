package sem.faculty.domain;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import lombok.Getter;
import sem.faculty.provider.TimeProvider;

@Getter
public class Faculty {

    //TODO: Replace ALLOCATED_RESOURCES with an actual bound after resource_manager is implemented.
    private static final long ALLOCATED_RESOURCES = 2;

    private FacultyName facultyName;
    private Map<LocalDate, List<Request>> schedule;
    private Queue<Request> pendingRequests;
    private final TimeProvider timeProvider;

    /**
     * Constructor method.
     * @param facultyName - FacultyName and represents the key to the faculty.
     * @param timeProvider - TimeProvider that provides the current time. It allows for easy mocking.
     */
    public Faculty(FacultyName facultyName, TimeProvider timeProvider) {
        this.facultyName = facultyName;
        this.timeProvider = timeProvider;
        schedule = new HashMap<>();
        pendingRequests = new LinkedList<>();
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
            if (checkAvailabilityForDate(request, scheduledDate)) {
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
    protected boolean checkAvailabilityForDate(Request request, LocalDate scheduledDate) {
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
        //TODO: reserveResources(request, scheduledDate); //Reserve the resources for request on the scheduledDate.
        schedule.put(scheduledDate, list);
    }

    /**
     * Add a Request to pendingRequests.
     * @param request - Request that will be added.
     */
    public void addPendingRequest(Request request) {
        pendingRequests.add(request);
    }
}
