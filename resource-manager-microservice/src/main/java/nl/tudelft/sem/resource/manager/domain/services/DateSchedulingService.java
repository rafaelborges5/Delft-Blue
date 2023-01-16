package nl.tudelft.sem.resource.manager.domain.services;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.providers.implementations.CurrentDateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service that handles getting the date for a request.
 */
@Service
@AllArgsConstructor
public class DateSchedulingService {
    private final transient CurrentDateProvider currentDateProvider;
    private final transient ResourceAvailabilityService resourceAvailabilityService;

    /**
     * Retrieves the closest available date when the given resources are available.
     *
     * @param resources the amount of resources to be reserved
     * @param date the date by which the resources need to be reserved
     * @param facultyName the name of the faculty for which the reservation should be made
     * @return a LocalDate that represents when the reservation can be made, or null
     *          if there is no opening by that date
     */
    LocalDate getDateForRequest(
            Resource resources,
            LocalDate date,
            Reserver facultyName) {
        LocalDate today = currentDateProvider.getCurrentDate();

        while (date.isAfter(today)) {
            Resource freeResources = resourceAvailabilityService.seeFreeResourcesByDateAndReserver(date, facultyName);

            if (enoughResourcesAvailable(freeResources, resources)) {

                return date;
            }
            date = date.minusDays(1);
        }

        return null;
    }

    private boolean enoughResourcesAvailable(Resource freeResources, Resource neededResources) {
        return  freeResources.getCpuResources() >= neededResources.getCpuResources() &&
                freeResources.getGpuResources() >= neededResources.getGpuResources() &&
                freeResources.getMemResources() >= neededResources.getMemResources();
    }
}
