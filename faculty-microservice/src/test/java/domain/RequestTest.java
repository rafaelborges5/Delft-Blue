package domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestTest {
    Request request;
    String name;
    String netId;
    String descr;
    LocalDate date;

    @BeforeEach
    void createRequest() {
        name = "Name";
        netId = "NETID";
        descr = "Description";
        date = LocalDate.of(2022, 12, 5);
        request = new Request(name, netId, descr, date, RequestStatus.PENDING);
    }

    @Test
    void testEmptyConstructor() {
        Request empty = new Request();
        assertThat(empty.getRequestId()).isEqualTo(0L);
        assertThat(empty.getNetId()).isNull();
        assertThat(empty.getName()).isNull();
        assertThat(empty.getDescription()).isNull();
        assertThat(empty.getPreferredDate()).isNull();
        assertThat(empty.getStatus()).isNull();
    }

    @Test
    void testToString() {
        String requestString = "Request{requestId='0', "
                + "netId='NETID', "
                + "name='Name', "
                + "description='Description', "
                + "preferredDate=2022-12-05, "
                + "status=PENDING"
                + "}";
        assertThat(request.toString()).isEqualTo(requestString);
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
    void getStatus() {
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
    }

    @Test
    void setNetId() {
        assertThat(request.getNetId()).isEqualTo(netId);
        request.setNetId("NEW NETID");
        assertThat(request.getNetId()).isEqualTo("NEW NETID");
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

    @Test
    void setStatus() {
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
        request.setStatus(RequestStatus.DENIED);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.DENIED);
    }
}