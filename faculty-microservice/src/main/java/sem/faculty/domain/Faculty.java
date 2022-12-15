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

        LocalDate scheduledDate = getScheduledDateFromResourceManager(request);

        //scheduledDate null means that there is no available day for scheduling
        if (scheduledDate == null) {
            throw new NotEnoughResourcesLeftException(request.getRequestId());
        }

        scheduleForDate(request, scheduledDate);
    }

    /**
     * Send the Request to Resource Manager and get back the date on which it can be scheduled.
     * @param request - Request to be sent to Resource Manager
     * @return the scheduled date on which the request can be run or null if there is no such date available.
     */
    private LocalDate getScheduledDateFromResourceManager(Request request) {
        LocalDate scheduledDate = request.getPreferredDate();
        Resource requiredResources = request.getResource();

        //TODO: send request to Resource Manager scheduled date
        // For now this is implemented manually
        LocalDate currentDate = timeProvider.getCurrentTime();
        while (!scheduledDate.isBefore(currentDate)) {
            if (checkAvailabilityForDate(request, scheduledDate)) {
                return scheduledDate;
            }
            scheduledDate = scheduledDate.minusDays(1);
        }

        return null;
    }

    /**
     * TODO: Remove this method when connection with Resource Manager is established
     * Check if a request can be scheduled on a given day.
     *
     * @param request       - Request that will be scheduled.
     * @param scheduledDate - Date on which to check if the request can be run.
     * @return true iff the request can be run on the given date.
     */
    protected boolean checkAvailabilityForDate(Request request, LocalDate scheduledDate) {
        List<Request> list = schedule.get(scheduledDate);
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
     * Choose how to handle Incoming Requests.
     * @param request - incoming Request.
     */
    public void handleIncomingRequest(Request request) {
        LocalDate currentDate = timeProvider.getCurrentTime();
        if (request.getPreferredDate().isBefore(currentDate) || getScheduledDateFromResourceManager(request) == null) {
            request.setStatus(RequestStatus.DENIED);
            return;
        }

        request.setStatus(RequestStatus.PENDING);
        pendingRequests.add(request);
    }
}
