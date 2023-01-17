package sem.faculty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories("sem.faculty.domain")
@ComponentScan(basePackages = {"sem.commons", "sem.faculty"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}