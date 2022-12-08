package provider;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

/**
 * Implementation of TimeProvider interface that returns the current date.
 * This is done to allow easy mocking.
 */
@Component
public class CurrentTimeProvider implements TimeProvider {

    public CurrentTimeProvider() {}

    @Override
    public LocalDate getCurrentTime() {
        return LocalDate.now();
    }
}
