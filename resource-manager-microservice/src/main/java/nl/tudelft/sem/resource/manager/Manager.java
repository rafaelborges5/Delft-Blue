package nl.tudelft.sem.resource.manager;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class Manager {
    private final transient DateProvider dateProvider;
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
    public LocalDate getDateForRequest(
            Resource resources,
            LocalDate date,
            Reserver facultyName) {
        LocalDate today = dateProvider.getCurrentDate();

        while (date.isAfter(today)) {
            Resource freeResources = resourceAvailabilityService.seeFreeResourcesByDateAndReserver(date, facultyName);

            if (freeResources.getCpuResources() >= resources.getCpuResources() &&
                    freeResources.getGpuResources() >= resources.getGpuResources() &&
                    freeResources.getMemResources() >= resources.getMemResources()) {

                return date;
            }
            date = date.minusDays(1);
        }

        return null;
    }
}
