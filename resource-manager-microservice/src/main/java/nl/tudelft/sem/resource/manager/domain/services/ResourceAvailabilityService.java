package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ResourceAvailabilityService {
    private final transient NodeRepository nodeRepository;
    private final transient ReservedResourcesRepository resourcesRepository;
    private final transient DateProvider timeProvider;
    private final transient FreepoolManager freepoolManager;
    private final transient DefaultResources defaultResources;

    /**
     * Instantiates a new {@link ResourceAvailabilityService}.
     *
     * @param nodeRepository the repository of all
     * {@link ClusterNode ClusterNodes} in the cluster
     * @param resourcesRepository the repository of resources assigned to
     * {@link Reserver Reserver} - {@link java.util.Date Date} pairs
     * @param timeProvider an interface for the time provider
     */
    public ResourceAvailabilityService(NodeRepository nodeRepository,
                                       ReservedResourcesRepository resourcesRepository,
                                       DateProvider timeProvider,
                                       FreepoolManager freepoolManager,
                                       DefaultResources defaultResources) {
        this.nodeRepository = nodeRepository;
        this.resourcesRepository = resourcesRepository;
        this.timeProvider = timeProvider;
        this.freepoolManager = freepoolManager;
        this.defaultResources = defaultResources;
    }

    /**
     * Returns the amount of free {@link Resource Resources} available the following day
     * for a given faculty. This also includes the resources available in the free pool.
     *
     * @param faculty the faculty for which to get the resources
     * @return the amount of free resources
     * @throws IllegalStateException if there are a negative amount of free resources
     */
    public Resource seeFreeResourcesTomorrow(Reserver faculty) {
        Resource initialResources = defaultResources.getInitialResources();

        LocalDate date = timeProvider.getCurrentDate().plusDays(1);
        Resource availableFreepoolResources = freepoolManager.getAvailableResources(date);
        Resource usedFacultyResources = resourcesRepository
                .findByReserverAndDate(faculty, date)
                .map(ReservedResources::getResources)
                .orElse(new Resource(0, 0, 0));

        return Resource.sub(Resource.add(initialResources, availableFreepoolResources), usedFacultyResources);
    }
}
