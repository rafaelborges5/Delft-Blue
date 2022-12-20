package sem.faculty.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sem.commons.FacultyName;
import sem.commons.NotValidResourcesException;
import sem.commons.Resource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;
    private Request request1;
    private Request request2;
    private Request request3;
    private Request request4;

    Resource resources;
    LocalDate time;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        resources = new Resource(1, 1, 1);
        time = LocalDate.of(2022, 12, 20);
        request1 = new Request(
                "name1", "user1", "description1",
                time, RequestStatus.PENDING, FacultyName.EEMCS, resources);
        request2 = new Request(
                "name2", "user1", "description3",
                time, RequestStatus.DROPPED, FacultyName.EEMCS, resources);
        request3 = new Request(
                "name3", "user3", "description3",
                time, RequestStatus.DENIED, FacultyName.EEMCS, resources);
        request4 = new Request(
                "name4", "user4", "description4",
                time, RequestStatus.ACCEPTED, FacultyName.EEMCS, resources);
        requestRepository.save(request1);
        requestRepository.save(request2);
        requestRepository.save(request3);
        requestRepository.save(request4);
    }


    @Test
    void findByNetId() {
        List<Request> toCheck = List.of(request1, request2);
        List<Request> returned = requestRepository.findByNetId("user1");
        assertThat(toCheck).isEqualTo(returned);
    }

    @Test
    void findPendingByRequestId() {
    }

    @Test
    void findByRequestId() {
    }

    @Test
    void updateRequestStatusDropped() {
    }

    @Test
    void updateRequestStatusPending() {
    }

    @Test
    void updateRequestStatusApproved() {
    }

    @Test
    void updateRequestStatusDenied() {
    }
}