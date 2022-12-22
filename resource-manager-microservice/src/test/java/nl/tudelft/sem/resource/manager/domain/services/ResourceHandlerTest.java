package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourceId;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.resource.exceptions.NotEnoughResourcesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ResourceHandlerTest {
    private transient ResourceHandler sut;
    private transient ReservedResourcesRepository reservedResourcesRepository;

    @BeforeEach
    void setUp() {
        reservedResourcesRepository = mock(ReservedResourcesRepository.class);
        sut = new ResourceHandler(reservedResourcesRepository);
    }

    @Test
    void update_reserved_resources_empty_initially_test() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Reserver faculty = Reserver.AE;
        when(reservedResourcesRepository.findById(new ReservedResourceId(date, faculty)))
                .thenReturn(Optional.empty());

        sut.updateReservedResources(date, faculty, Resource.with(50));

        ArgumentCaptor<ReservedResources> arg = ArgumentCaptor.forClass(ReservedResources.class);
        verify(reservedResourcesRepository).save(arg.capture());
        assertThat(arg.getValue())
                .isEqualTo(new ReservedResources(
                        new ReservedResourceId(date, faculty),
                        Resource.with(50)
                ));
    }

    @Test
    void update_reserved_resources_with_initial_reserved_resources_test() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Reserver faculty = Reserver.AE;
        when(reservedResourcesRepository.findById(new ReservedResourceId(date, faculty)))
                .thenReturn(Optional.of(new ReservedResources(
                        new ReservedResourceId(date, faculty),
                        Resource.with(40)
                )));

        sut.updateReservedResources(date, faculty, Resource.with(50));

        ArgumentCaptor<ReservedResources> arg = ArgumentCaptor.forClass(ReservedResources.class);
        verify(reservedResourcesRepository).save(arg.capture());
        assertThat(arg.getValue())
                .isEqualTo(new ReservedResources(
                        new ReservedResourceId(date, faculty),
                        Resource.with(90)
                ));
    }

    @Test
    void update_reserved_resources_negative_amount() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Reserver faculty = Reserver.AE;
        when(reservedResourcesRepository.findById(new ReservedResourceId(date, faculty)))
                .thenReturn(Optional.of(new ReservedResources(
                        new ReservedResourceId(date, faculty),
                        Resource.with(40)
                )));

        sut.updateReservedResources(date, faculty, Resource.with(-50));

        ArgumentCaptor<ReservedResources> arg = ArgumentCaptor.forClass(ReservedResources.class);
        verify(reservedResourcesRepository).save(arg.capture());
        assertThat(arg.getValue())
                .isEqualTo(new ReservedResources(
                        new ReservedResourceId(date, faculty),
                        Resource.with(-10)
                ));
    }
}