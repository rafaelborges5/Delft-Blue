package nl.tudelft.sem.resource.manager.domain.manager;

import nl.tudelft.sem.resource.manager.Manager;
import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourceId;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.resource.exceptions.NotEnoughResourcesException;
import nl.tudelft.sem.resource.manager.domain.services.FreepoolManager;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import nl.tudelft.sem.resource.manager.domain.services.ResourceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ReserveResourcesTest {
    private transient Manager sut;
    private transient ResourceHandler resourceHandler;
    private transient ResourceAvailabilityService resourceAvailabilityService;
    private transient ReservedResourcesRepository reservedResourcesRepository;
    private transient DefaultResources defaultResources;
    private transient DateProvider dateProvider;
    private transient NodeRepository nodeRepository;
    private transient FreepoolManager freepoolManager;

    @BeforeEach
    void setUp() {
        resourceAvailabilityService = mock(ResourceAvailabilityService.class);
        reservedResourcesRepository = mock(ReservedResourcesRepository.class);
        defaultResources = new DefaultResources(100);
        resourceHandler = new ResourceHandler(reservedResourcesRepository);
        dateProvider = mock(DateProvider.class);
        nodeRepository = mock(NodeRepository.class);
        freepoolManager = mock(FreepoolManager.class);

        sut = new Manager(
                dateProvider,
                resourceAvailabilityService,
                nodeRepository,
                reservedResourcesRepository,
                freepoolManager,
                defaultResources,
                resourceHandler
        );
    }

    @Test
    void reserve_resources_on_day_test() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Reserver faculty = Reserver.AE;
        ReservedResourceId facultyId = new ReservedResourceId(date, Reserver.AE);
        ReservedResourceId poolId = new ReservedResourceId(date, Reserver.FREEPOOL);

        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(date, faculty))
                .thenReturn(new Resource(50, 100, 150));
        when(reservedResourcesRepository.findById(facultyId))
                .thenReturn(Optional.of(new ReservedResources(
                                facultyId,
                                new Resource(80, 50, 50))
                //              Free resources: 20, 50, 50
                ));
        try {
            sut.reserveResourcesOnDay(faculty, new Resource(50, 40, 50), date);
        } catch (NotEnoughResourcesException e) {
            e.printStackTrace();
        }

        ArgumentCaptor<ReservedResources> arg = ArgumentCaptor.forClass(ReservedResources.class);

        verify(reservedResourcesRepository, times(2)).save(arg.capture());
        assertThat(arg.getAllValues()).containsExactlyInAnyOrder(
                new ReservedResources(facultyId, new Resource(100, 90, 100)),
                new ReservedResources(poolId, new Resource(30, 0, 0))
        );
    }

    @Test
    void reserve_resources_on_day_insufficient_resources_test() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Reserver faculty = Reserver.AE;
        ReservedResourceId facultyId = new ReservedResourceId(date, Reserver.AE);

        when(resourceAvailabilityService.seeFreeResourcesByDateAndReserver(date, faculty))
                .thenReturn(new Resource(50, 100, 150));
        when(reservedResourcesRepository.findById(facultyId))
                .thenReturn(Optional.of(new ReservedResources(
                                facultyId,
                                new Resource(80, 50, 50))
                //              Free resources: 20, 50, 50
                ));

        assertThatThrownBy(() -> {
            sut.reserveResourcesOnDay(faculty, new Resource(60, 40, 50), date);
        }).isInstanceOf(NotEnoughResourcesException.class);
    }

    @Test
    void release_resources_on_days() {
        LocalDate day1 = LocalDate.of(2022, 1, 1);
        LocalDate day2 = LocalDate.of(2022, 1, 2);
        Reserver faculty = Reserver.AE;

        ReservedResourceId id1 = new ReservedResourceId(day1, faculty);
        ReservedResourceId id2 = new ReservedResourceId(day2, faculty);

        when(reservedResourcesRepository.findById(id1))
                .thenReturn(Optional.of(new ReservedResources(id1, Resource.with(10))));
        when(reservedResourcesRepository.findById(id2))
                .thenReturn(Optional.of(new ReservedResources(id2, Resource.with(20))));

        sut.releaseResourcesOnDays(faculty, List.of(day1, day2));

        ArgumentCaptor<ReservedResources> arg = ArgumentCaptor.forClass(ReservedResources.class);
        verify(reservedResourcesRepository, times(4)).save(arg.capture());

        assertThat(arg.getAllValues()).containsExactlyInAnyOrder(
                new ReservedResources(id1, Resource.with(100)),
                new ReservedResources(id2, Resource.with(100)),
                new ReservedResources(new ReservedResourceId(day1, Reserver.FREEPOOL), Resource.with(-90)),
                new ReservedResources(new ReservedResourceId(day2, Reserver.FREEPOOL), Resource.with(-80))
        );
    }

    @Test
    void release_resources_only_some_days() {
        LocalDate day1 = LocalDate.of(2022, 1, 1);
        LocalDate day2 = LocalDate.of(2022, 1, 2);
        Reserver faculty = Reserver.AE;

        ReservedResourceId id1 = new ReservedResourceId(day1, faculty);
        ReservedResourceId id2 = new ReservedResourceId(day2, faculty);

        when(reservedResourcesRepository.findById(id1))
                .thenReturn(Optional.of(new ReservedResources(id1, Resource.with(10))));
        when(reservedResourcesRepository.findById(id2))
                .thenReturn(Optional.of(new ReservedResources(id2, Resource.with(20))));

        sut.releaseResourcesOnDays(faculty, List.of(day1));

        ArgumentCaptor<ReservedResources> arg = ArgumentCaptor.forClass(ReservedResources.class);
        verify(reservedResourcesRepository, times(2)).save(arg.capture());

        assertThat(arg.getAllValues()).containsExactlyInAnyOrder(
                new ReservedResources(id1, Resource.with(100)),
                new ReservedResources(new ReservedResourceId(day1, Reserver.FREEPOOL), Resource.with(-90))
        );
    }
}
