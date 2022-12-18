package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.*;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FreepoolManagerTest {
    private transient FreepoolManager sut;
    private transient DefaultResources defaultResources;
    private transient ReservedResourcesRepository resourcesRepository;
    private transient NodeRepository nodeRepository;

    @BeforeEach
    private void setup() {
        defaultResources = new DefaultResources();
        resourcesRepository = Mockito.mock(ReservedResourcesRepository.class);
        nodeRepository = Mockito.mock(NodeRepository.class);
        sut = new FreepoolManager(defaultResources, resourcesRepository, nodeRepository);
    }

    @Test
    void get_available_freepool_resources() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Resource res1 = new Resource(100, 50, 20);

        Mockito.when(resourcesRepository.findByReserverAndDate(Reserver.FREEPOOL, date))
                .thenReturn(Optional.of(new ReservedResources(date, Reserver.FREEPOOL, res1)));

        ClusterNode node1 = new ClusterNode(
                new OwnerName("name1"),
                new URL("url1"),
                new Token("token1"),
                new Resource(60, 60, 60));

        ClusterNode node2 = new ClusterNode(
                new OwnerName("name2"),
                new URL("url2"),
                new Token("token2"),
                new Resource(40, 40, 40));

        Mockito.when(nodeRepository.findAll()).thenReturn(List.of(node1, node2));

        Resource availableResources = sut.getAvailableResources(date);

        Resource clusterResources = Resource.add(node1.getResources(), node2.getResources());

        assertThat(availableResources)
                .isEqualTo(Resource.sub(clusterResources, res1));
    }

    @Test
    void get_available_resources_empty_cluster() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Resource res1 = new Resource(100, 50, 20);

        Mockito.when(resourcesRepository.findByReserverAndDate(Reserver.FREEPOOL, date))
                .thenReturn(Optional.of(new ReservedResources(date, Reserver.FREEPOOL, res1)));

        Mockito.when(nodeRepository.findAll()).thenReturn(List.of());

        Resource availableResources = sut.getAvailableResources(date);

        assertThat(availableResources)
                .isEqualTo(Resource.sub(new Resource(0, 0, 0), res1));
    }

    @Test
    void get_available_resources_no_reserved_resources() {
        LocalDate date = LocalDate.of(2022, 1, 1);

        Mockito.when(resourcesRepository.findByReserverAndDate(Reserver.FREEPOOL, date))
                .thenReturn(Optional.empty());

        ClusterNode node1 = new ClusterNode(
                new OwnerName("name1"),
                new URL("url1"),
                new Token("token1"),
                new Resource(60, 60, 60));

        ClusterNode node2 = new ClusterNode(
                new OwnerName("name2"),
                new URL("url2"),
                new Token("token2"),
                new Resource(40, 40, 40));

        Mockito.when(nodeRepository.findAll()).thenReturn(List.of(node1, node2));

        Resource availableResources = sut.getAvailableResources(date);

        assertThat(availableResources)
                .isEqualTo(Resource.add(node1.getResources(), node2.getResources()));
    }

    @Test
    void update_available_resource() {

    }
}