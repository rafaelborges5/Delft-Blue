package sem.faculty.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sem.faculty.provider.CurrentTimeProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FacultyManager {

    private Map<FacultyName, Faculty> faculties;

    public FacultyManager() {
        faculties = new HashMap<>();
        faculties.put(FacultyName.EEMCS, new Faculty(FacultyName.EEMCS, new CurrentTimeProvider()));
        faculties.put(FacultyName.AE, new Faculty(FacultyName.AE, new CurrentTimeProvider()));
        faculties.put(FacultyName.AS, new Faculty(FacultyName.AS, new CurrentTimeProvider()));
        faculties.put(FacultyName.CEG, new Faculty(FacultyName.CEG, new CurrentTimeProvider()));
        faculties.put(FacultyName.ARCH, new Faculty(FacultyName.ARCH, new CurrentTimeProvider()));
        faculties.put(FacultyName.IDE, new Faculty(FacultyName.IDE, new CurrentTimeProvider()));
        faculties.put(FacultyName.MMME, new Faculty(FacultyName.MMME, new CurrentTimeProvider()));
        faculties.put(FacultyName.TPM, new Faculty(FacultyName.TPM, new CurrentTimeProvider()));
    }

    @Bean
    public FacultyManager newFacultyManager() {
        return new FacultyManager();
    }

    public List<Request> getPendingRequests(FacultyName facultyName) {
        Faculty faculty = faculties.get(facultyName);
        //TODO: get requests from given faculty
        return new ArrayList<>();
    }
}
