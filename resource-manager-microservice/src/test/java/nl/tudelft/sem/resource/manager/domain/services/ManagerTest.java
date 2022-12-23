package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.node.exceptions.NodeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sem.commons.OwnerName;
import sem.commons.Token;
import sem.commons.URL;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ManagerTest {
    private transient Manager sut;
    @Mock private transient DateSchedulingService dateSchedulingService;
    @Mock private transient ResourceHandler resourceHandler;
    @Mock private transient ResourceAvailabilityService resourceAvailabilityService;
    @Mock private transient NodeRepository nodeRepository;

    private transient ClusterNode node1;
    private transient ClusterNode node2;


    @BeforeEach
    void setUp() {
        sut = new Manager(dateSchedulingService, resourceHandler, resourceAvailabilityService, nodeRepository);

        node1 = new ClusterNode(
                new OwnerName("name1"),
                new URL("url1"),
                new Token("token1"),
                Resource.with(50)
        );

        node2 = new ClusterNode(
                new OwnerName("name2"),
                new URL("url2"),
                new Token("token2"),
                Resource.with(60)
        );
    }

    @Test
    void see_cluster_node_information_test() {
        when(nodeRepository.findAll()).thenReturn(List.of(node1, node2));

        assertThat(sut.seeClusterNodeInformation())
                .containsExactlyInAnyOrder(node1, node2);
    }

    @Test
    void add_node_to_cluster_test() {
        when(nodeRepository.existsByToken(node1.getToken())).thenReturn(false);

        assertThat(sut.addNodeToCluster(node1)).isEqualTo("OK");

        verify(nodeRepository, times(1)).save(node1);
    }

    @Test
    void add_node_to_cluster_fail_test() {
        when(nodeRepository.existsByToken(node1.getToken())).thenReturn(true);

        assertThat(sut.addNodeToCluster(node1)).isNotEqualTo("OK");

        verify(nodeRepository, times(0)).save(node1);
    }

    @Test
    void remove_node_from_cluster_test() throws NodeNotFoundException {
        when(nodeRepository.existsByToken(node1.getToken())).thenReturn(true);

        sut.removeNodeFromCluster(node1.getToken());

        verify(nodeRepository, times(1)).removeByToken(node1.getToken());
    }

    @Test
    void remove_node_from_cluster_fails_test() throws NodeNotFoundException {
        when(nodeRepository.existsByToken(node1.getToken())).thenReturn(false);

        assertThatThrownBy(() -> {
            sut.removeNodeFromCluster(node1.getToken());
        }).isInstanceOf(NodeNotFoundException.class);

        verify(nodeRepository, times(0)).removeByToken(node1.getToken());
    }
}