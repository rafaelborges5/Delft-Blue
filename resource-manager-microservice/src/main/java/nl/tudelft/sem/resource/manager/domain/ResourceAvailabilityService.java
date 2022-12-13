package nl.tudelft.sem.resource.manager.domain;

import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.node.OwnerName;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.AssignedResources;
import nl.tudelft.sem.resource.manager.domain.resource.AssignedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Assignee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ResourceAvailabilityService {
    private final transient NodeRepository nodeRepository;
    private final transient AssignedResourcesRepository resourcesRepository;
    private final transient DateProvider timeProvider;

    /**
     * Instantiates a new {@link ResourceAvailabilityService}.
     *
     * @param nodeRepository the repository of all
     * {@link nl.tudelft.sem.resource.manager.domain.node.ClusterNode ClusterNode} in the cluster
     * @param resourcesRepository the repository of resources assigned to
     * {@link nl.tudelft.sem.resource.manager.domain.resource.Assignee Assignee} - {@link java.util.Date Date} pairs
     * @param timeProvider an interface for the time provider
     */
    public ResourceAvailabilityService(NodeRepository nodeRepository,
                                       AssignedResourcesRepository resourcesRepository,
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
    public Resource seeFreeResourcesTomorrow(Assignee faculty) throws IllegalStateException {
        LocalDate date = timeProvider.getCurrentDate().plusDays(1);

        OwnerName facultyName = new OwnerName(faculty.name());
        OwnerName freepoolName = new OwnerName(Assignee.FREEPOOL.name());

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
                .findByAssigneeAndDate(faculty, date)
                .map(AssignedResources::getResources)
                .orElse(new Resource(0, 0, 0));
        Resource poolRes = resourcesRepository
                .findByAssigneeAndDate(Assignee.FREEPOOL, date)
                .map(AssignedResources::getResources)
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
