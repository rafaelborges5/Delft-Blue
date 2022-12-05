package domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestTest {
    Request request;
    String name;
    String descr;
    LocalDate date;

    @BeforeEach
    void createRequest() {
        name = "Name";
        descr = "Description";
        date = LocalDate.of(2022, 12, 5);
        request = new Request(name, descr, date, Status.PENDING);
    }

    @Test
    void testToString() {
        String requestString = "Request{name='Name', "
                + "description='Description', "
                + "preferredDate=2022-12-05, "
                + "status=PENDING}";
        assertThat(request.toString()).isEqualTo(requestString);
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
        assertThat(request.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void setStatus() {
        assertThat(request.getStatus()).isEqualTo(Status.PENDING);
        request.setStatus(Status.DENIED);
        assertThat(request.getStatus()).isEqualTo(Status.DENIED);
    }
}