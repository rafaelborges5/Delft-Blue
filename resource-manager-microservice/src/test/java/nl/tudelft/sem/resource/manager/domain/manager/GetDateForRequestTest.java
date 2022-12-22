package nl.tudelft.sem.resource.manager.domain.manager;

import nl.tudelft.sem.resource.manager.Manager;
import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.FreepoolManager;
import nl.tudelft.sem.resource.manager.domain.services.NodeHandler;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import nl.tudelft.sem.resource.manager.domain.services.ResourceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetDateForRequestTest {
    private transient Manager sut;
    private transient ResourceAvailabilityService resourceAvailabilityService;
    private transient NodeRepository nodeRepository;
    private transient ReservedResourcesRepository reservedResourcesRepository;
    private transient DateProvider dateProvider;
    private transient FreepoolManager freepoolManager;
    private transient DefaultResources defaultResources;
    private transient ResourceHandler resourceHandler;
    private transient NodeHandler nodeHandler;

    @BeforeEach
    void setUp() {
        dateProvider = mock(DateProvider.class);
        resourceAvailabilityService = mock(ResourceAvailabilityService.class);
        nodeRepository = mock(NodeRepository.class);
        reservedResourcesRepository = mock(ReservedResourcesRepository.class);
        freepoolManager = mock(FreepoolManager.class);
        defaultResources = mock(DefaultResources.class);
        resourceHandler = mock(ResourceHandler.class);
        nodeHandler = mock(NodeHandler.class);

        sut = new Manager(
                dateProvider,
                resourceAvailabilityService,
                nodeRepository,
                reservedResourcesRepository,
                freepoolManager,
                defaultResources,
                resourceHandler,
                nodeHandler
        );
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
        Resource neededResources = Resource.with(70);

        LocalDate chosenDate = sut.getDateForRequest(neededResources, afterTomorrow, faculty);

        assertThat(chosenDate).isEqualTo(tomorrow);
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
