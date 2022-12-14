package nl.tudelft.sem.template.authentication.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/messages")
public class UserRequestController {

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public UserRequestController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(value = "/sendUserRequest")
    public void sendUserRequest(@RequestBody Object requestDTO) throws JsonProcessingException {
        System.out.println("got a request");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestDTO = objectMapper.writeValueAsString(requestDTO);

        kafkaTemplate.send("example", jsonRequestDTO);
    }
}