package nl.tudelft.sem.template.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Example microservice application.
 */
@SpringBootApplication(scanBasePackages = "nl.tudelft.sem.template.gateway.config")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
