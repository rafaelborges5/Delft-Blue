package nl.tudelft.sem.resource.manager.domain.controllers;

import nl.tudelft.sem.resource.manager.Manager;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.exceptions.NodeNotFoundException;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.NodeHandler;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sem.commons.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class NodeClusterController {

    private final transient Manager manager;

    private final transient NodeHandler nodeHandler;

    @Autowired
    public NodeClusterController(Manager manager, NodeHandler nodeHandler) {
        this.manager = manager;
        this.nodeHandler = nodeHandler;
    }


    /**
     * This method will allow users to see the amount of available resources for their faculties for the following day.
     * @param record the Consumer Record
     * @param faculties the Package that contains all the faculties the user belongs
     * @return A Regular user view, which is basically a map from faculty name to resources available
     */
    @KafkaListener(
            topics = "user-view",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryFacultyNamePackageDTO"
    )
    @SendTo
    public RegularUserView getUserViewResourcesForDate(ConsumerRecord<String, FacultyNamePackageDTO> record,
                                                       @Payload FacultyNamePackageDTO faculties) {
        Map<FacultyNameDTO, sem.commons.Resource> map = new HashMap<>();
        faculties.getFaculties().forEach(x -> {
            Resource resourceObject = manager
                    .seeFreeResourcesTomorrow(Reserver.valueOf(x.getFacultyName().toUpperCase(Locale.UK)));

            try {
                map.put(x, new sem.commons.Resource(resourceObject.getCpuResources(), resourceObject.getGpuResources(),
                        resourceObject.getMemResources()));
            } catch (NotValidResourcesException e) {
                throw new RuntimeException(e);
            }
        });
        return new RegularUserView(map);
    }


    /**
     * This method will be the kafka endpoint to add new cluster nodes to the resource manager.
     * It will receive a DTO that represents the cluster node to add to the system
     * @param record the consumer record
     * @param clusterNode the DTO representing the node to add
     */
    @KafkaListener(
            topics = "add-node",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryClusterNodeDTO"
    )
    @SendTo
    public String addClusterNode(ConsumerRecord<String, ClusterNodeDTO> record, @Payload ClusterNodeDTO clusterNode) {
        return nodeHandler.addNodeToCluster(new ClusterNode(clusterNode.getOwnerName(), clusterNode.getUrl(),
                clusterNode.getToken(),
                new Resource(clusterNode.getResources().getCpu(), clusterNode.getResources().getGpu(),
                        clusterNode.getResources().getMemory())));
    }


    /**
     * This method will be the kafka endpoint to remove cluster nodes from the system.
     * It will receive a DTO that identifies the node to remove
     * @param record the consumer record
     * @param token the token that identifies the node
     * @return a success/failure message
     */
    @KafkaListener(
            topics = "remove-node",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryTokenDTO"
    )
    @SendTo
    public String removeClusterNode(ConsumerRecord<String, String> record, @Payload Token token) {
        try {
            nodeHandler.removeNodeFromCluster(token);
            return "Node removed successfully";
        } catch (NodeNotFoundException e) {
            return "There are no nodes with that token!";
        }
    }





}
