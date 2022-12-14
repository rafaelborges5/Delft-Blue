package sem.faculty.handler;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.FacultyName;
import sem.faculty.domain.Request;
import sem.faculty.provider.CurrentTimeProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class FacultyHandler {
    Map<FacultyName, Faculty> faculties;

    public FacultyHandler() {
        faculties = new HashMap<>();
        populateFaculties();
    }

    @Bean
    public FacultyHandler newFacultyHandler() {
        return new FacultyHandler();
    }

    /**
     * Create Faculty instances for each FacultyName.
     */
    private void populateFaculties() {
        faculties.clear();
        for (FacultyName fn : FacultyName.values()) {
            faculties.put(fn, new Faculty(fn, new CurrentTimeProvider()));
        }
    }


    /**
     * Listen for incoming Requests.
     */
    @KafkaListener(
            topics = "incoming-request",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactory2"
    )
    void listener(Request request) {
        faculties.get(request.getFacultyName()).handleIncomingRequest(request);
    }


    /**
     * Gets pending requests.
     *
     * @param facultyName the faculty name
     * @return the pending requests
     */
    public List<Request> getPendingRequests(FacultyName facultyName) {
        Faculty faculty = faculties.get(facultyName);
        //TODO: get requests from given faculty
        return new ArrayList<>();
    }
}