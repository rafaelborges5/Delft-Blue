package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.authentication.AuthManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import sem.commons.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClusterNodeControllerTest {

    ReplyingKafkaTemplate<String, ClusterNodeDTO, String> kafkaTemplate;
    AuthManager authManager;
    ClusterNodeController clusterNodeController;
    ClusterNodeDTO clusterNodeDTO;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        kafkaTemplate = Mockito.mock(ReplyingKafkaTemplate.class);
        authManager = Mockito.mock(AuthManager.class);
        clusterNodeController = new ClusterNodeController(kafkaTemplate, authManager);
        clusterNodeDTO = new ClusterNodeDTO(new Token("token"), new OwnerName("name"),
                new URL("url"), new Resource(3, 2, 1));
    }

    @Test
    void addClusterNodeSuccess() throws ExecutionException, InterruptedException, TimeoutException {
        RequestReplyFuture<String, ClusterNodeDTO, String> future = Mockito.mock(RequestReplyFuture.class);
        ConsumerRecord<String, String> consumerRecord =
                new ConsumerRecord<>("test", 1, 1L, "test", "The node was added successfully");
        when(future.get(10, TimeUnit.SECONDS)).thenReturn(consumerRecord);
        when(kafkaTemplate.sendAndReceive(any())).thenReturn(future);
        when(authManager.getNetId()).thenReturn("name");

        ResponseEntity<String> returned = clusterNodeController.addClusterNode(clusterNodeDTO);
        assertThat(returned.getBody()).isEqualTo("The node was added successfully");

    }

    @Test
    void addClusterNodeDenied() throws ExecutionException, InterruptedException, TimeoutException {
        Mockito.when(authManager.getNetId()).thenReturn("diffName");
        ResponseEntity<String> returned = clusterNodeController.addClusterNode(clusterNodeDTO);
        assertThat(returned.getBody()).isEqualTo("You can only submit new nodes in your own name!");
        Mockito.verify(kafkaTemplate, Mockito.never()).send("add-node", clusterNodeDTO);
    }
}