package nl.tudelft.sem.resource.manager.domain.manager;

import nl.tudelft.sem.resource.manager.Manager;
import nl.tudelft.sem.resource.manager.domain.DefaultResources;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.providers.DateProvider;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourceId;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResources;
import nl.tudelft.sem.resource.manager.domain.resource.ReservedResourcesRepository;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.FreepoolManager;
import nl.tudelft.sem.resource.manager.domain.services.NodeHandler;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import nl.tudelft.sem.resource.manager.domain.services.ResourceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sem.commons.OwnerName;
import sem.commons.Token;
import sem.commons.URL;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAdminViewTest {
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
        nodeRepository = mock(NodeRepository.class);
        reservedResourcesRepository = mock(ReservedResourcesRepository.class);
        dateProvider = mock(DateProvider.class);
        resourceHandler = mock(ResourceHandler.class);
        nodeHandler = mock(NodeHandler.class);
        when(dateProvider.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));

        defaultResources = new DefaultResources(100);

        freepoolManager = new FreepoolManager(
                defaultResources,
                reservedResourcesRepository,
                nodeRepository
        );

        resourceAvailabilityService = new ResourceAvailabilityService(
                reservedResourcesRepository,
                freepoolManager,
                defaultResources
        );

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

        LocalDate date = LocalDate.of(2020, 1, 1);
        ReservedResources reservedResourcesAS = new ReservedResources(
                new ReservedResourceId(date, Reserver.AS),
                new Resource(10, 10, 10)
        );
        ReservedResources reservedResourcesARCH = new ReservedResources(
                new ReservedResourceId(date, Reserver.ARCH),
                new Resource(20, 20, 20)
        );
        ReservedResources reservedResourcesPOOL = new ReservedResources(
                new ReservedResourceId(date, Reserver.FREEPOOL),
                new Resource(30, 30, 30)
        );

        LocalDate tmrw = LocalDate.of(2022, 1, 2);
        ReservedResources reservedResourcesArchTmrw = new ReservedResources(
                new ReservedResourceId(tmrw, Reserver.ARCH),
                new Resource(40, 40, 40)
        );
        ReservedResources reservedResourcesPoolTmrw = new ReservedResources(
                new ReservedResourceId(tmrw, Reserver.FREEPOOL),
                new Resource(50, 50, 50)
        );

        when(reservedResourcesRepository.findAllById_Date(date))
                .thenReturn(List.of(
                        reservedResourcesAS,
                        reservedResourcesARCH,
                        reservedResourcesPOOL
                ));
        when(reservedResourcesRepository.findAllById_Date(tmrw))
                .thenReturn(List.of(
                        reservedResourcesArchTmrw,
                        reservedResourcesPoolTmrw
                ));
        when(reservedResourcesRepository.findById(new ReservedResourceId(date, Reserver.FREEPOOL)))
                .thenReturn(Optional.of(reservedResourcesPOOL));
        when(reservedResourcesRepository.findById(new ReservedResourceId(tmrw, Reserver.FREEPOOL)))
                .thenReturn(Optional.of(reservedResourcesPoolTmrw));
        when(reservedResourcesRepository.findById(new ReservedResourceId(tmrw, Reserver.ARCH)))
                .thenReturn(Optional.of(reservedResourcesArchTmrw));

        ClusterNode node1 = new ClusterNode(
                new OwnerName("name1"),
                new URL("url1"),
                new Token("token1"),
                new Resource(50, 50, 50)
        );
        ClusterNode node2 = new ClusterNode(
                new OwnerName("name2"),
                new URL("url2"),
                new Token("token2"),
                new Resource(60, 60, 60)
        );
        when(nodeRepository.findAll())
                .thenReturn(List.of(node1, node2));
    }


    @Test
    void free_resources_on_date_test() {
        assertThat(sut.seeFreeResourcesOnDate(LocalDate.of(2020, 1, 1)))
                .isEqualTo(new Resource(250, 250, 250));
    }

    @Test
    void see_free_resources_tomorrow_test() {
        assertThat(sut.seeFreeResourcesTomorrow(Reserver.ARCH))
                .isEqualTo(new Resource(120, 120, 120));
    }

    @Test
    void see_cluster_node_information_test() {
        assertThat(sut.seeClusterNodeInformation())
                .containsExactlyInAnyOrder(
                        new ClusterNode(
                                new OwnerName("name1"),
                                new URL("url1"),
                                new Token("token1"),
                                new Resource(50, 50, 50)
                        ), new ClusterNode(
                                new OwnerName("name2"),
                                new URL("url2"),
                                new Token("token2"),
                                new Resource(60, 60, 60)
                        ));
    }

    @Test
    void see_reserved_resources_on_date_test() {
        assertThat(sut.seeReservedResourcesOnDate(LocalDate.of(2020, 1, 1)))
                .isEqualTo(new Resource(60, 60, 60));
    }
}