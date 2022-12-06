package provider;

import java.time.LocalDate;

public interface TimeProvider {
    /**
     * Retrieves the current time.
     *
     * @return The current time
     */
    LocalDate getCurrentTime();
}
