package nl.tudelft.sem.resource.manager.controllers;

import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.exceptions.NodeNotFoundException;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.Manager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sem.commons.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class NodeClusterControllerTest {
    private transient Manager manager;
    private transient NodeClusterController nodeClusterController;
    private transient FacultyNameDTO facultyNameDTO;
    private transient Resource resource;
    private transient RegularUserView regularUserView;
    private transient FacultyNamePackageDTO facultyNamePackageDTO;
    private transient ClusterNodeDTO clusterNodeDTO;
    private transient DateDTO dateDTO;
    private transient LocalDate localDate;
    private transient List<ClusterNodeDTO> clusterNodeDTOList;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        manager = Mockito.mock(Manager.class);
        nodeClusterController = new NodeClusterController(manager);
        facultyNameDTO = new FacultyNameDTO("EEMCS");
        resource = new Resource(3, 2, 1);
        regularUserView = new RegularUserView(Map.of(facultyNameDTO, resource));
        facultyNamePackageDTO = new FacultyNamePackageDTO(List.of(facultyNameDTO));
        clusterNodeDTO = new ClusterNodeDTO(new Token("token"), new OwnerName("name"),
                new URL("url"), new Resource(3, 2, 1));
        dateDTO = new DateDTO(2022, 12, 25);
        localDate = LocalDate.of(dateDTO.getYear(), dateDTO.getMonth(), dateDTO.getDay());
        clusterNodeDTOList = new ArrayList<>();
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
        Mockito.verify(manager).addNodeToCluster(clusterNode);
    }

    @Test
    void testRemoveClusterNodeValid() {
        Token token = new Token("test");
        try {
            manager.removeNodeFromCluster(token);
        } catch (NodeNotFoundException e) {
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    void testRemoveClusterNodeNotValid() throws NodeNotFoundException {
        Token token = new Token("test");
        NodeNotFoundException exception = new NodeNotFoundException(token);
        Mockito.doThrow(exception)
                .when(manager)
                .removeNodeFromCluster(token);
        assertThat(nodeClusterController.removeClusterNode(Mockito.any(), token))
                .isEqualTo("There are no nodes with that token!");
    }
}