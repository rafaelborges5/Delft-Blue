package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service that handles the logic behind interfaces consumed by the faculty-microservice.
 * These include:
 * <ul>
 *     <li>Retrieving the first available date for a Request.</li>
 *     <li>
 *         Informing the faculty-microservice that X amount of resources
 *         on a given day need to be freed by dropping requests.
 *     </li>
 * </ul>
 */
@Service
public class DateSchedulingService {

    /**
     * Retrieves the first available date when the given resources are available.
     *
     * @param resources the amount of resources to be reserved
     * @param date the date by which the resources need to be reserved
     * @param facultyName the name of the faculty for which the reservation should be made
     * @return a LocalDate that represents when the reservation can be made, or null
     *          if there is no opening by that date
     */
    LocalDate getDateForRequest(Resource resources,
                                        LocalDate date,
                                        Reserver facultyName) {
        return LocalDate.of(2022, 1, 1);
    }


}
