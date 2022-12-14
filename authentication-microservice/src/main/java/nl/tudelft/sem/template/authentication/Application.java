package nl.tudelft.sem.template.authentication;

import nl.tudelft.sem.template.authentication.controllers.UserRequestController;
import nl.tudelft.sem.template.authentication.tobeimportedfromothermodules.FacultyName;
import nl.tudelft.sem.template.authentication.tobeimportedfromothermodules.NotValidResourcesException;
import nl.tudelft.sem.template.authentication.tobeimportedfromothermodules.RequestDTO;
import nl.tudelft.sem.template.authentication.tobeimportedfromothermodules.Resource;
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

    /**
     * This is a temporary function.
     * @return reqeustDTO
     * @throws NotValidResourcesException - blah
     */
    //TODO: this function, and the example above should be deleted once valid tests are in place. ~ Jasper
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

