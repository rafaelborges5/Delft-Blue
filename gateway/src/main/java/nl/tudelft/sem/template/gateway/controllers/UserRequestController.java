package nl.tudelft.sem.template.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.RequestDTO;

@RestController
public class UserRequestController {

    private transient KafkaTemplate<String, RequestDTO> kafkaTemplate;

    @Autowired
    public UserRequestController(KafkaTemplate<String, RequestDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a request trough kafka to the "incoming-request" topic.
     * Catches JsonProcessingException - when the requestDTO cannot be converted to json
     *
     * @param requestDTO - the DTO to be send
     */
    @PostMapping("request/new")
    public ResponseEntity<String> sendUserRequest(@RequestBody RequestDTO requestDTO) {
        kafkaTemplate.send("incoming-request", requestDTO);
        return ResponseEntity.ok("Request was send");
    }
}