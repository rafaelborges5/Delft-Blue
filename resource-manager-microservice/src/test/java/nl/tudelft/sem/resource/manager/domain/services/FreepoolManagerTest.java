package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.*;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourceId;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
//@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FreepoolManagerTest {
    @Autowired
    private transient FreepoolManager sut;
    @Autowired
    private transient DefaultResources defaultResources;
    @Autowired
    private transient ReservedResourcesRepository resourcesRepository;
    @Autowired
    private transient NodeRepository nodeRepository;

    @BeforeEach
    void setUp() {
        sut = new FreepoolManager(defaultResources, resourcesRepository, nodeRepository);
    }

    @Test
    void get_available_freepool_resources() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Resource res1 = new Resource(100, 50, 20);

        resourcesRepository.save(new ReservedResources(new ReservedResourceId(date, Reserver.FREEPOOL), res1));

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

        nodeRepository.saveAll(List.of(node1, node2));

        Resource availableResources = sut.getAvailableResources(date);

        assertThat(availableResources)
                .isEqualTo(new Resource(0, 50, 80));
    }

    @Test
    void get_available_resources_empty_cluster() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Resource res1 = new Resource(100, 50, 20);

        resourcesRepository.save(new ReservedResources(new ReservedResourceId(date, Reserver.FREEPOOL), res1));

        Resource availableResources = sut.getAvailableResources(date);

        assertThat(availableResources)
                .isEqualTo(new Resource(-100, -50, -20));
    }

    @Test
    void get_available_resources_no_reserved_resources() {
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

        nodeRepository.saveAll(List.of(node1, node2));

        LocalDate date = LocalDate.of(2022, 1, 1);
        Resource availableResources = sut.getAvailableResources(date);

        assertThat(availableResources)
                .isEqualTo(new Resource(100, 100, 100));
    }
}