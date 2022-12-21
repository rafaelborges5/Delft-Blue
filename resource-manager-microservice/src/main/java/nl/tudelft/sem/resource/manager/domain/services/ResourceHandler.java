package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.resource.exceptions.NotEnoughResourcesException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service that handles reserving resources for a request, and releasing a faculty's
 * resources for a time period. Also provides a callback that checks whether a node
 * being removed makes it so there are insufficient resources left for the planned
 * requests
 */
@Service
public class ResourceHandler {

    /**
     * Reserve resources for a request from a faculty on a given date.
     * May reserve resources from the freepool if there are not enough free resources
     * allocated to the faculty
     *
     * @param faculty the faculty for which to reserve resources from
     * @param resourceAmount the amount of resources to reserve
     * @param date the date on which to reserve the resources
     * @throws NotEnoughResourcesException if there are not enough resources on that day
     */
    public void reserveResourcesOnDay(Reserver faculty,
                                      Resource resourceAmount,
                                      LocalDate date) throws NotEnoughResourcesException {

    }

    /**
     * Releases a faculty's resources on a list of days. This is done by
     * adding the amount of free resources left for the faculty on that day,
     * and subtracting it from the freepool's reserved resources.
     * @param faculty the faculty that has frees its resources
     * @param releasedDays the days on which to release the resources
     */
    public void releaseResourcesOnDays(Reserver faculty,
                                       List<LocalDate> releasedDays) {

    }

    /**
     * Checks if there are enough resources in the freepool, on all days
     * with reserved resources. This should be used as a callback in the
     * event that a cluster node is deleted.
     */
    public void checkNotEnoughFreepoolResources() {

    }
}
