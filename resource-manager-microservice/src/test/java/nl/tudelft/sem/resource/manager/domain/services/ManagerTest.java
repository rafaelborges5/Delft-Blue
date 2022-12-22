package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sem.commons.OwnerName;
import sem.commons.Token;
import sem.commons.URL;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ManagerTest {
    private transient Manager sut;
    @Mock private transient DateSchedulingService dateSchedulingService;
    @Mock private transient ResourceHandler resourceHandler;
    @Mock private transient ResourceAvailabilityService resourceAvailabilityService;
    @Mock private transient NodeRepository nodeRepository;


    @BeforeEach
    void setUp() {
        sut = new Manager(dateSchedulingService, resourceHandler, resourceAvailabilityService, nodeRepository);
    }

    @Test
    void see_cluster_node_information_test() {
        when(nodeRepository.findAll()).thenReturn(List.of(
                new ClusterNode(
                        new OwnerName("name1"),
                        new URL("url1"),
                        new Token("token1"),
                        Resource.with(50)
                ), new ClusterNode(
                        new OwnerName("name2"),
                        new URL("url2"),
                        new Token("token2"),
                        Resource.with(60)
                )
        ));

        assertThat(sut.seeClusterNodeInformation())
                .containsExactlyInAnyOrder(
                        new ClusterNode(
                                new OwnerName("name1"),
                                new URL("url1"),
                                new Token("token1"),
                                new Resource(50, 50, 50)
                        ), new ClusterNode(
                                new OwnerName("name2"),
                                new URL("url2"),
                                new Token("token2"),
                                new Resource(60, 60, 60)
                        ));
    }
}