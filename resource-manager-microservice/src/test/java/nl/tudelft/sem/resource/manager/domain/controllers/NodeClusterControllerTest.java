package nl.tudelft.sem.resource.manager.domain.controllers;

import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sem.commons.*;


import static org.assertj.core.api.Assertions.assertThat;


import java.util.Map;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class NodeClusterControllerTest {

    private transient ResourceAvailabilityService resourceAvailabilityService;
    private transient NodeClusterController nodeClusterController;
    private transient FacultyNameDTO facultyNameDTO;
    private transient Resource resource;
    private transient RegularUserView regularUserView;
    private transient FacultyNamePackageDTO facultyNamePackageDTO;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        resourceAvailabilityService = Mockito.mock(ResourceAvailabilityService.class);
        nodeClusterController = new NodeClusterController(resourceAvailabilityService);
        facultyNameDTO = new FacultyNameDTO("EEMCS");
        resource = new Resource(3, 2, 1);
        regularUserView = new RegularUserView(Map.of(facultyNameDTO, resource));
        facultyNamePackageDTO = new FacultyNamePackageDTO(List.of(facultyNameDTO));
    }

    @Test
    void getUserViewResourcesForDate() {
        Mockito.when(resourceAvailabilityService.seeFreeResourcesTomorrow(Reserver.EEMCS))
                .thenReturn(new nl.tudelft.sem.resource.manager.domain.Resource(3, 2, 1));
        assertThat(nodeClusterController.getUserViewResourcesForDate(
                new ConsumerRecord<>("test", 1, 1L, "test", facultyNamePackageDTO), facultyNamePackageDTO))
                .isEqualTo(regularUserView);
    }
}