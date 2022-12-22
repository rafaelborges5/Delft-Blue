package nl.tudelft.sem.resource.manager;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.FreepoolManager;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import nl.tudelft.sem.resource.manager.domain.services.ResourceHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class Manager {
    private final transient DateProvider dateProvider;
    private final transient ResourceAvailabilityService resourceAvailabilityService;
    private final transient NodeRepository nodeRepository;
    private final transient ReservedResourcesRepository reservedResourcesRepository;
    private final transient FreepoolManager freepoolManager;
    private final transient DefaultResources defaultResources;
    private final transient ResourceHandler resourceHandler;


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



    /**
     * Returns the free resources on a given date.
     * @param date the date on which to get the free resources
     * @return the free resources
     */
    public Resource seeFreeResourcesOnDate(LocalDate date) {
        Resource freeFacultyResources = reservedResourcesRepository
                .findAllById_Date(date)
                .stream()
                .filter(rr -> !rr.getId().getReserver().equals(Reserver.FREEPOOL))
                .map(ReservedResources::getResources)
                .map(r -> Resource.sub(defaultResources.getInitialResources(), r))
                .reduce(new Resource(), Resource::add);

        Resource freeFreepoolResources = freepoolManager.getAvailableResources(date);

        return Resource.add(freeFreepoolResources, freeFacultyResources);
    }


    /**
     * Returns the amount of free {@link Resource Resources} available the following day
     * for a given faculty. This also includes the resources available in the free pool.
     *
     * @param faculty the faculty for which to get the resources
     * @return the amount of free resources
     */
    public Resource seeFreeResourcesTomorrow(Reserver faculty) {
        LocalDate tomorrow = dateProvider.getCurrentDate().plusDays(1);
        return resourceAvailabilityService.seeFreeResourcesByDateAndReserver(tomorrow, faculty);
    }

    /**
     * Returns a list of the cluster nodes, for the sysadmin view.
     * @return a list of cluster nodes
     */
    public List<ClusterNode> seeClusterNodeInformation() {
        return nodeRepository
                .findAll();
    }

    /**
     * Returns the reserved resources on a given date.
     * @param date the date on which to get the reserved resources
     * @return the amount of reserved resources
     */
    public Resource seeReservedResourcesOnDate(LocalDate date) {
        return reservedResourcesRepository
                .findAllById_Date(date)
                .stream()
                .map(ReservedResources::getResources)
                .reduce(new Resource(), Resource::add);
    }
}
