package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Manages the resources in the free pool.
 */
@Service
public class FreepoolManager {
    private final transient DefaultResources defaultResources;
    private final transient ReservedResourcesRepository resourcesRepository;
    private final transient NodeRepository nodeRepository;

    /**
     * Injects dependencies.
     *
     * @param defaultResources DefaultResources
     * @param resourcesRepository ResourcesRepository
     * @param nodeRepository NodeRepository
     */
    @Autowired
    public FreepoolManager(DefaultResources defaultResources,
                           ReservedResourcesRepository resourcesRepository,
                           NodeRepository nodeRepository) {
        this.defaultResources = defaultResources;
        this.resourcesRepository = resourcesRepository;
        this.nodeRepository = nodeRepository;
    }

    /**
     * Returns how many resources are available in the freepool at a given date.
     * @param date a {@link LocalDate} representing the date to check for
     * @return what Resources are available
     */
    public Resource getAvailableResources(LocalDate date) {
        Resource freepoolSize = nodeRepository
                .findAll()
                .stream()
                .map(ClusterNode::getResources)
                .reduce(new Resource(), (r1, r2) -> {
                    if (r2 == null) {
                        return r1;
                    }

                    r1.setCpuResources(r1.getCpuResources() + r2.getCpuResources());
                    r1.setGpuResources(r1.getGpuResources() + r2.getGpuResources());
                    r1.setMemResources(r1.getMemResources() + r2.getMemResources());
                    return r1;
                });

        Resource usedResources = resourcesRepository
                .findByReserverAndDate(Reserver.FREEPOOL, date)
                .map(ReservedResources::getResources)
                .orElse(new Resource(0, 0, 0));

        return Resource.sub(freepoolSize, usedResources);
    }
}
