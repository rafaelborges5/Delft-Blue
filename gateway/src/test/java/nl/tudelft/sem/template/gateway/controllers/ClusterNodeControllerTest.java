package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.authentication.AuthManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import sem.commons.*;

import static org.assertj.core.api.Assertions.assertThat;

class ClusterNodeControllerTest {

    KafkaTemplate<String, ClusterNodeDTO> kafkaTemplate;
    AuthManager authManager;
    ClusterNodeController clusterNodeController;
    ClusterNodeDTO clusterNodeDTO;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        authManager = Mockito.mock(AuthManager.class);
        clusterNodeController = new ClusterNodeController(kafkaTemplate, authManager);
        clusterNodeDTO = new ClusterNodeDTO(new Token("token"), new OwnerName("name"),
                new URL("url"), new Resource(3, 2, 1));
    }

    @Test
    void addClusterNodeSuccess() {
        Mockito.when(authManager.getNetId()).thenReturn("name");
        ResponseEntity<String> returned = clusterNodeController.addClusterNode(clusterNodeDTO);
        assertThat(returned.getBody()).isEqualTo("The node was added successfully");
        Mockito.verify(kafkaTemplate).send("add-node", clusterNodeDTO);
    }

    @Test
    void addClusterNodeDenied() {
        Mockito.when(authManager.getNetId()).thenReturn("diffName");
        ResponseEntity<String> returned = clusterNodeController.addClusterNode(clusterNodeDTO);
        assertThat(returned.getBody()).isEqualTo("You can only submit new nodes in your own name!");
        Mockito.verify(kafkaTemplate, Mockito.never()).send("add-node", clusterNodeDTO);
    }
}