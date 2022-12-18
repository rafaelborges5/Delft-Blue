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
     * Add the request on the specified date in the schedule.
     *
     * @param request       - Request that is added to schedule.
     * @param scheduledDate - LocalDate on which the request is scheduled.
     */
    public void scheduleForDate(Request request, LocalDate scheduledDate) {
        List<Request> list = schedule.get(scheduledDate);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(request);
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
