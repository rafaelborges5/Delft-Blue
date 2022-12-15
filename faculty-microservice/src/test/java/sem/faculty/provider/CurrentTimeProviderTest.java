package sem.faculty.provider;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class CurrentTimeProviderTest {
    CurrentTimeProvider currentTimeProvider = new CurrentTimeProvider();

    @Test
    void testGetCurrentTime() {
        //Can't properly test this class, so I'll just do this for coverage:)
        assertThat(LocalDate.now()).isEqualTo(currentTimeProvider.getCurrentTime());
    }
}