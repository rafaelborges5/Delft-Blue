package nl.tudelft.sem.template.authentication;

import nl.tudelft.sem.template.authentication.controllers.UserRequestController;
import nl.tudelft.sem.template.authentication.toBeImportedFromOtherModule.FacultyName;
import nl.tudelft.sem.template.authentication.toBeImportedFromOtherModule.NotValidResourcesException;
import nl.tudelft.sem.template.authentication.toBeImportedFromOtherModule.RequestDTO;
import nl.tudelft.sem.template.authentication.toBeImportedFromOtherModule.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

@SpringBootApplication()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, Object> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send("example", "Test5");

            UserRequestController urc = new UserRequestController(kafkaTemplate);
            RequestDTO requestDTO = createRequestDTO();
            urc.sendUserRequest(requestDTO);
        };
    }

    public RequestDTO createRequestDTO() throws NotValidResourcesException {
        Long id = 0L;
        String name = "Name";
        String netId = "NETID";
        FacultyName faculty = FacultyName.EEMCS;
        String descr = "Description";
        LocalDate date = LocalDate.of(2022, 12, 5);
        Resource resource = new Resource(1, 1, 1);
        return new RequestDTO(id, name, netId, faculty, descr, date, resource);
    }
}

