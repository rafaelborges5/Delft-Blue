package handler;

import domain.Faculty;
import domain.FacultyName;
import domain.Request;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import provider.CurrentTimeProvider;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class FacultyHandler {
    Map<FacultyName, Faculty> faculties;

    public FacultyHandler() {
        faculties = new HashMap<>();
        populateFaculties();
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
            groupId = "requests",
            containerFactory = "kafkaListenerContainerFactory2"
    )
    void listener(Request request) {
        faculties.get(request.getFacultyName()).handleIncomingRequest(request);
    }
}
