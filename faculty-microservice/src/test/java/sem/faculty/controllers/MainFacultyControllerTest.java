package sem.faculty.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sem.commons.AcceptRequestsDTO;
import sem.commons.FacultyNameDTO;
import sem.faculty.handler.FacultyHandlerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MainFacultyControllerTest {

    private FacultyHandlerService facultyHandlerService;

    private MainFacultyController mainFacultyController;

    @BeforeEach
    void setUp() {
        facultyHandlerService = Mockito.mock(FacultyHandlerService.class);
        mainFacultyController = new MainFacultyController(facultyHandlerService);
    }

    @Test
    void getPendingRequests() {
        mainFacultyController.getPendingRequests(new FacultyNameDTO("EEMCS"));
        verify(facultyHandlerService, times(1)).getPendingRequests("EEMCS");
    }

    @Test
    void acceptRequests() {
        AcceptRequestsDTO acceptRequestsDTO = new AcceptRequestsDTO("EEMCS", List.of(1L));
        mainFacultyController.acceptRequests(acceptRequestsDTO);
        verify(facultyHandlerService, times(1)).acceptRequests("EEMCS", acceptRequestsDTO.getAcceptedRequests());
    }
}