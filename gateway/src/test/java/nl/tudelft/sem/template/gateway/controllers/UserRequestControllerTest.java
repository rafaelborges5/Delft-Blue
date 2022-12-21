package nl.tudelft.sem.template.gateway.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import sem.commons.*;

import java.time.LocalDate;

public class UserRequestControllerTest {

    private UserRequestController userRequestController;
    private RequestDTO requestDTO;

    @Mock
    KafkaTemplate<String, RequestDTO> kafkaTemplate;

    @BeforeEach
    void setup() throws NotValidResourcesException {
        requestDTO = new RequestDTO(1L, "name", "netId", FacultyName.EEMCS, "desc",
                LocalDate.of(2015, 2, 3), new Resource(1, 1, 1));
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        userRequestController = new UserRequestController(kafkaTemplate);
    }

    @Test
    void sendUserRequestTest() {
        ResponseEntity<String> responseEntity = userRequestController.sendUserRequest(requestDTO);
        ResponseEntity<String> expected = ResponseEntity.ok("Request was send");

        assert (responseEntity.equals(expected));
        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send(Mockito.eq("incoming-request"), Mockito.any(RequestDTO.class));
    }
}
