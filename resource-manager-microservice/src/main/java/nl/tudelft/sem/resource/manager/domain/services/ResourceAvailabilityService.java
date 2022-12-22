package nl.tudelft.sem.resource.manager.domain.services;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourceId;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ResourceAvailabilityService {
    private final transient ReservedResourcesRepository reservedResourcesRepository;
    private final transient FreepoolManager freepoolManager;
    private final transient DefaultResources defaultResources;

    /**
     * Returns the amount of free Resources available to the reserver, on the given date.
     *
     * @param date the date on which to get the amount of free resources
     * @param reserver the entity for which to check the amount of free resources
     * @return the amount of free Resources
     */
    public Resource seeFreeResourcesByDateAndReserver(LocalDate date, Reserver reserver) {
        Resource initialResources = defaultResources.getInitialResources();
        Resource usedFacultyResources = reservedResourcesRepository
                .findById(new ReservedResourceId(date, reserver))
                .map(ReservedResources::getResources)
                .orElse(new Resource(0, 0, 0));
        Resource freeFacultyResources = Resource.sub(initialResources, usedFacultyResources);

        Resource freeFreepoolResources = freepoolManager.getAvailableResources(date);

        return Resource.add(freeFacultyResources, freeFreepoolResources);
    }
}
