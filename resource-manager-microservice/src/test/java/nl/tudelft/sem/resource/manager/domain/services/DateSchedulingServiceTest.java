package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateSchedulingServiceTest {
    private transient DateSchedulingService sut;
    private transient DateProvider dateProvider;
    private transient ResourceAvailabilityService resourceAvailabilityService;

    @BeforeEach
    void setUp() {
        dateProvider = mock(DateProvider.class);
        resourceAvailabilityService = mock(ResourceAvailabilityService.class);
        sut = new DateSchedulingService(dateProvider, resourceAvailabilityService);
    }

    @Test
    void get_date_for_request_test() {
        LocalDate today = LocalDate.of(2022, 1, 1);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate afterTomorrow = today.plusDays(2);
        Reserver faculty = Reserver.AE;
        when(dateProvider.getCurrentDate()).thenReturn(today);
        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(today, faculty))
                .thenReturn(Resource.with(150));
        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(tomorrow, faculty))
                .thenReturn(Resource.with(100));
        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(afterTomorrow, faculty))
                .thenReturn(Resource.with(50));
        Resource neededResources = Resource.with(150);

        LocalDate chosenDate = sut.getDateForRequest(neededResources, afterTomorrow, faculty);

        assertThat(chosenDate).isEqualTo(today);
    }

    @Test
    void no_available_date_for_request_test() {
        LocalDate today = LocalDate.of(2022, 1, 1);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate afterTomorrow = today.plusDays(2);
        Reserver faculty = Reserver.AE;
        when(dateProvider.getCurrentDate()).thenReturn(today);
        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(today, faculty))
                .thenReturn(Resource.with(150));
        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(tomorrow, faculty))
                .thenReturn(Resource.with(100));
        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(afterTomorrow, faculty))
                .thenReturn(Resource.with(50));
        Resource neededResources = Resource.with(160);

        LocalDate chosenDate = sut.getDateForRequest(neededResources, afterTomorrow, faculty);

        assertThat(chosenDate).isNull();
    }
}