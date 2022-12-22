package nl.tudelft.sem.resource.manager.domain.services;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourceId;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
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
@AllArgsConstructor
public class ResourceHandler {
    private final transient ReservedResourcesRepository reservedResourcesRepository;

    /**
     * Checks if there are enough resources in the freepool, on all days
     * with reserved resources. This should be used as a callback in the
     * event that a cluster node is deleted.
     */
    public void checkNotEnoughFreepoolResources() {

    }

    /**
     * Updates the amount of reserved resources by adding the newResources to
     * the already existing ones.
     *
     * @param date the date for the ReservedResrouces
     * @param reserver the reserver name for the ReservedResources
     * @param newResources the amount of resources to add
     */
    public void updateReservedResources(LocalDate date, Reserver reserver, Resource newResources) {
        Resource currentResources = reservedResourcesRepository
                .findById(new ReservedResourceId(date, reserver))
                .map(ReservedResources::getResources)
                .orElse(Resource.with(0));

        reservedResourcesRepository.save(new ReservedResources(
                new ReservedResourceId(date, reserver),
                Resource.add(currentResources, newResources)
        ));
    }
}
