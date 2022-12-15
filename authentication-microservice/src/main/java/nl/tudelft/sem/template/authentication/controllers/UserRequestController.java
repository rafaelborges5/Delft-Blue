package nl.tudelft.sem.template.authentication.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/messages")
public class UserRequestController {

    private transient KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public UserRequestController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a request trough kafka to the "incoming-request" topic.
     *
     * @param requestDTO - the DTO to be send
     * @throws JsonProcessingException - when the requestDTO cannot be converted to json
     */
    @PostMapping(value = "/sendUserRequest")
    public void sendUserRequest(@RequestBody Object requestDTO) throws JsonProcessingException {
        System.out.println("about to send a request in DTO form");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestDTO = objectMapper.writeValueAsString(requestDTO);

        System.out.println(jsonRequestDTO);
        kafkaTemplate.send("incoming-request", jsonRequestDTO);
    }
}