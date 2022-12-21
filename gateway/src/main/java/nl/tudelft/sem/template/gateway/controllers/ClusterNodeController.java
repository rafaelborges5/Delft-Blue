package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.authentication.AuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.ClusterNodeDTO;

@RestController
@RequestMapping("clusterNode/")
public class ClusterNodeController {

    private final transient KafkaTemplate<String, ClusterNodeDTO> kafkaTemplateClusterNodeDTO;
    private final transient AuthManager authManager;

    @Autowired
    public ClusterNodeController(KafkaTemplate<String, ClusterNodeDTO> kafkaTemplateClusterNodeDTO,
                                 AuthManager authManager) {
        this.kafkaTemplateClusterNodeDTO = kafkaTemplateClusterNodeDTO;
        this.authManager = authManager;
    }

    /**
     * This method is the api endpoint to add cluster nodes to the system.
     * @param clusterNodeDTO the DTO representing the cluster node to add
     * @return a ResponseEntity specifying weather or not the node was added successfully
     */
    @PostMapping("/add")
    public ResponseEntity<String> addClusterNode(@RequestBody ClusterNodeDTO clusterNodeDTO) {
        if (!clusterNodeDTO.getOwnerName().getName().equals(authManager.getNetId())) {
            return ResponseEntity.status(401).body("You can only submit new nodes in your own name!");
        }
        kafkaTemplateClusterNodeDTO.send("add-node", clusterNodeDTO);
        return ResponseEntity.ok("The node was added successfully");
    }

}
