package nl.tudelft.sem.template.gateway.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RequestTest {
    RequestDTO requestDTO;
    String name;
    String netId;
    String descr;
    LocalDate date;
    Resource resource;

    @BeforeEach
    void createRequest() {
        name = "Name";
        netId = "NETID";
        descr = "Description";
        date = LocalDate.of(2022, 12, 5);
        resource = new Resource(1, 1, 1);
        requestDTO = new RequestDTO(name, netId, descr, date, resource);
    }

    @Test
    void testEmptyConstructor() {
        RequestDTO empty = new RequestDTO();
        assertThat(empty.getRequestId()).isEqualTo(0L);
        assertThat(empty.getNetId()).isNull();
        assertThat(empty.getName()).isNull();
        assertThat(empty.getDescription()).isNull();
        assertThat(empty.getPreferredDate()).isNull();
        assertThat(empty.getStatus()).isNull();
        assertThat(empty.getResource()).isNull();
    }

    @Test
    void testConstructor() {
        assertThat(request.getRequestId()).isEqualTo(0L);
        assertThat(request.getNetId()).isEqualTo(netId);
        assertThat(request.getName()).isEqualTo(name);
        assertThat(request.getDescription()).isEqualTo(descr);
        assertThat(request.getPreferredDate()).isEqualTo(date);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
        assertThat(request.getResource()).isEqualTo(resource);
    }
    }


    @Test
    void getRequestId() {
        assertThat(request.getRequestId()).isEqualTo(0L);
    }

    @Test
    void getNetId() {
        assertThat(request.getNetId()).isEqualTo(netId);
    }

    @Test
    void getName() {
        assertThat(request.getName()).isEqualTo(name);
    }

    @Test
    void getDescription() {
        assertThat(request.getDescription()).isEqualTo(descr);
    }

    @Test
    void getPreferredDate() {
        assertThat(request.getPreferredDate()).isEqualTo(date);
    }

    @Test
    void getResource() {
        assertThat(request.getResource()).isEqualTo(resource);
    }

    @Test
    void setNetId() {
        assert(request.getNetId()).isEqualTo(netId);
        request.setNetId("NEW NETID");
        assert(request.getNetId()).isEqualTo("NEW NETID");
    }

    @Test
    void setName() {
        assertThat(request.getName()).isEqualTo(name);
        request.setName("NEW NAME");
        assertThat(request.getName()).isEqualTo("NEW NAME");
    }

    @Test
    void setDescription() {
        assertThat(request.getDescription()).isEqualTo(descr);
        request.setDescription("NEW DESCRIPTION");
        assertThat(request.getDescription()).isEqualTo("NEW DESCRIPTION");
    }

    @Test
    void setPreferredDate() {
        assertThat(request.getPreferredDate()).isEqualTo(date);
        LocalDate newDate = LocalDate.of(2022, 12, 6);
        request.setPreferredDate(newDate);
        assertThat(request.getPreferredDate()).isEqualTo(newDate);
    }
}
