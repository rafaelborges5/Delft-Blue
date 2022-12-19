package nl.tudelft.sem.resource.manager.domain;

import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.node.OwnerName;
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
                                       DateProvider timeProvider) {
        this.nodeRepository = nodeRepository;
        this.resourcesRepository = resourcesRepository;
        this.timeProvider = timeProvider;
    }

    /**
     * Returns the amount of free {@link Resource Resources} available the following day
     * for a given faculty. This also includes the resources available in the free pool.
     *
     * @param faculty the faculty for which to get the resources
     * @return the amount of free resources
     * @throws IllegalStateException if there are a negative amount of free resources
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Resource seeFreeResourcesTomorrow(Reserver faculty) throws IllegalStateException {
        LocalDate date = timeProvider.getCurrentDate().plusDays(1);

        OwnerName facultyName = new OwnerName(faculty.name());
        OwnerName freepoolName = new OwnerName(Reserver.FREEPOOL.name());

        Resource availableRes = nodeRepository
                .findAllByOwnerNameOrOwnerName(facultyName, freepoolName)
                .stream()
                .map(ClusterNode::getResources)
                .reduce(new Resource(), (r1, r2) -> {
                    r1.setCpuResources(r1.getCpuResources() + r2.getCpuResources());
                    r1.setGpuResources(r1.getGpuResources() + r2.getGpuResources());
                    r1.setMemResources(r1.getMemResources() + r2.getMemResources());
                    return r1;
                });

        Resource facRes = resourcesRepository
                .findByReserverAndDate(faculty, date)
                .map(ReservedResources::getResources)
                .orElse(new Resource(0, 0, 0));
        Resource poolRes = resourcesRepository
                .findByReserverAndDate(Reserver.FREEPOOL, date)
                .map(ReservedResources::getResources)
                .orElse(new Resource(0, 0, 0));

        Resource freeResources = new Resource(
                availableRes.getCpuResources() - poolRes.getCpuResources() - facRes.getCpuResources(),
                availableRes.getGpuResources() - poolRes.getGpuResources() - facRes.getCpuResources(),
                availableRes.getMemResources() - poolRes.getMemResources() - facRes.getMemResources()
        );

        if (freeResources.getMemResources() < 0
                || freeResources.getGpuResources() < 0
                || freeResources.getCpuResources() < 0) {
            throw new IllegalStateException("Free resources cannot be negative");
        }

        return freeResources;
    }
}
