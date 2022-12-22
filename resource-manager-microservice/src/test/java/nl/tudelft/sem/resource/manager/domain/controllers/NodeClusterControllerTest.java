package nl.tudelft.sem.resource.manager.domain.controllers;

import javassist.NotFoundException;
import nl.tudelft.sem.resource.manager.Manager;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.exceptions.NodeNotFoundException;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.NodeHandler;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sem.commons.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class NodeClusterControllerTest {
    private transient Manager manager;
    private transient ResourceAvailabilityService resourceAvailabilityService;
    private transient NodeClusterController nodeClusterController;

    private transient NodeHandler nodeHandler;
    private transient FacultyNameDTO facultyNameDTO;
    private transient Resource resource;
    private transient RegularUserView regularUserView;
    private transient FacultyNamePackageDTO facultyNamePackageDTO;
    private transient ClusterNodeDTO clusterNodeDTO;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        resourceAvailabilityService = Mockito.mock(ResourceAvailabilityService.class);
        nodeHandler = Mockito.mock(NodeHandler.class);
        manager = Mockito.mock(Manager.class);
        nodeClusterController = new NodeClusterController(manager, nodeHandler);
        facultyNameDTO = new FacultyNameDTO("EEMCS");
        resource = new Resource(3, 2, 1);
        regularUserView = new RegularUserView(Map.of(facultyNameDTO, resource));
        facultyNamePackageDTO = new FacultyNamePackageDTO(List.of(facultyNameDTO));
        clusterNodeDTO = new ClusterNodeDTO(new Token("token"), new OwnerName("name"),
                new URL("url"), new Resource(3, 2, 1));
    }

    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    void getUserViewResourcesForDate() {
        Mockito.when(manager.seeFreeResourcesTomorrow(Reserver.EEMCS))
                .thenReturn(new nl.tudelft.sem.resource.manager.domain.Resource(3, 2, 1));
        assertThat(nodeClusterController.getUserViewResourcesForDate(
                new ConsumerRecord<>("test", 1, 1L, "test", facultyNamePackageDTO), facultyNamePackageDTO))
                .isEqualTo(regularUserView);
    }

    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    void testAddClusterNode() {
        ClusterNode clusterNode = new ClusterNode(clusterNodeDTO.getOwnerName(), clusterNodeDTO.getUrl(),
                clusterNodeDTO.getToken(),
                new nl.tudelft.sem.resource.manager.domain.Resource(clusterNodeDTO.getResources().getCpu(),
                        clusterNodeDTO.getResources().getGpu(), clusterNodeDTO.getResources().getMemory()));
        ConsumerRecord<String, ClusterNodeDTO> record = new ConsumerRecord<>("test", 1, 1, "test", clusterNodeDTO);
        nodeClusterController.addClusterNode(record, clusterNodeDTO);
        Mockito.verify(nodeHandler).addNodeToCluster(clusterNode);
    }

    @Test
    void testRemoveClusterNodeValid() {
        Token token = new Token("test");
        try {
            nodeHandler.removeNodeFromCluster(token);
        } catch (NodeNotFoundException e) {
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    void testRemoveClusterNodeNotValid() throws NodeNotFoundException {
        Token token = new Token("test");
        NodeNotFoundException exception = new NodeNotFoundException(token);
        Mockito.doThrow(exception)
                .when(nodeHandler)
                .removeNodeFromCluster(token);
        assertThat(nodeClusterController.removeClusterNode(Mockito.any(), token))
                .isEqualTo("There are no nodes with that token!");
    }
}