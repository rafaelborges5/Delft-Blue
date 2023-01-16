package sem.faculty.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sem.commons.FacultyName;
import sem.commons.NotValidResourcesException;
import sem.commons.Resource;

class RequestTest {
    Request request;
    String name;
    String netId;
    String descr;
    LocalDate date;
    Resource resource;

    @BeforeEach
    void createRequest() throws NotValidResourcesException {
        name = "Name";
        netId = "NETID";
        descr = "Description";
        date = LocalDate.of(2022, 12, 5);
        resource = new Resource(1, 1, 1);
        request = new Request(name, netId, descr, date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
    }

    @Test
    void testEmptyConstructor() {
        Request empty = new Request();
        assertThat(empty.getRequestId()).isEqualTo(0L);
        assertThat(empty.getRequestFacultyInformation()).isNull();
        assertThat(empty.getRequestResourceManagerInformation()).isNull();
    }

    @Test
    void testConstructor() {
        assertThat(request.getRequestId()).isEqualTo(0L);
        assertThat(request.getRequestFacultyInformation().getNetId()).isEqualTo(netId);
        assertThat(request.getRequestResourceManagerInformation().getName()).isEqualTo(name);
        assertThat(request.getRequestResourceManagerInformation().getDescription()).isEqualTo(descr);
        assertThat(request.getRequestFacultyInformation().getPreferredDate()).isEqualTo(date);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
        assertThat(request.getRequestResourceManagerInformation().getResource()).isEqualTo(resource);
    }

    @Test
    void testToString() {
        String requestString = "Request{requestId=0, "
                + "status=PENDING, "
                + "netId='NETID', "
                + "name='Name', "
                + "description='Description', "
                + "preferredDate=2022-12-05, "
                + "faculty=EEMCS, "
                + "resource='Resource(cpu=1, gpu=1, memory=1)'"
                + "}";
        assertThat(request.toString()).isEqualTo(requestString);
    }

    @Test
    void testEquals() {
        Request r1 = new Request(name, netId, descr, date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        r1.setRequestId(0);
        assertThat(r1).isEqualTo(request);
        assertThat(request).isEqualTo(request);
    }

    @Test
    void testNotEquals() throws NotValidResourcesException {
        Request r1 = new Request("Other", netId, descr, date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        assertThat(request).isNotEqualTo(r1);

        Request r2 = new Request(name, "Other", descr, date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        assertThat(request).isNotEqualTo(r2);

        Request r3 = new Request(name, netId, "Other", date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        assertThat(request).isNotEqualTo(r3);

        LocalDate notDate = LocalDate.of(1990, 10, 1);
        Request r4 = new Request(name, netId, descr, notDate, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        assertThat(request).isNotEqualTo(r4);

        Request r5 = new Request(name, netId, descr, date, RequestStatus.ACCEPTED, FacultyName.EEMCS, resource);
        assertThat(request).isNotEqualTo(r5);

        Request r6 = new Request(name, netId, descr, date, RequestStatus.PENDING, FacultyName.ARCH, resource);
        assertThat(request).isNotEqualTo(r6);

        Request r7 = new Request(name, netId, descr, date, RequestStatus.PENDING, FacultyName.EEMCS,
                new Resource(5, 1, 1));
        assertThat(request).isNotEqualTo(r7);

        Request r8 = new Request(name, netId, descr, date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        r8.setRequestId(5);
        assertThat(request).isNotEqualTo(r8);
    }

    @Test
    void testNotEqualsObject() {
        String str = "not a request";
        assertThat(request.equals(str)).isFalse();
    }

    @Test
    public void equalsHashCode() {
        Request r = new Request(name, netId, descr, date, RequestStatus.PENDING, FacultyName.EEMCS, resource);
        assertThat(request).isEqualTo(r);
        assertThat(r.hashCode()).isEqualTo(request.hashCode());
    }

    @Test
    void getRequestId() {
        assertThat(request.getRequestId()).isEqualTo(0L);
    }

    @Test
    void getNetId() {
        assertThat(request.getRequestFacultyInformation().getNetId()).isEqualTo(netId);
    }

    @Test
    void getName() {
        assertThat(request.getRequestResourceManagerInformation().getName()).isEqualTo(name);
    }

    @Test
    void getDescription() {
        assertThat(request.getRequestResourceManagerInformation().getDescription()).isEqualTo(descr);
    }

    @Test
    void getPreferredDate() {
        assertThat(request.getRequestFacultyInformation().getPreferredDate()).isEqualTo(date);
    }

    @Test
    void getStatus() {
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
    }

    @Test
    void getResource() {
        assertThat(request.getRequestResourceManagerInformation().getResource()).isEqualTo(resource);
    }

    @Test
    void setNetId() {
        assertThat(request.getRequestFacultyInformation().getNetId()).isEqualTo(netId);
        request = new Request(request.getRequestResourceManagerInformation().getName(), "NEW NETID",
                request.getRequestResourceManagerInformation().getDescription(),
                request.getRequestFacultyInformation().getPreferredDate(), request.getStatus(),
                request.getRequestFacultyInformation().getFaculty(),
                request.getRequestResourceManagerInformation().getResource());
        assertThat(request.getRequestFacultyInformation().getNetId()).isEqualTo("NEW NETID");
    }

    @Test
    void setName() {
        assertThat(request.getRequestResourceManagerInformation().getName()).isEqualTo(name);
        request = new Request("NEW NAME", request.getRequestFacultyInformation().getNetId(),
                request.getRequestResourceManagerInformation().getDescription(),
                request.getRequestFacultyInformation().getPreferredDate(), request.getStatus(),
                request.getRequestFacultyInformation().getFaculty(),
                request.getRequestResourceManagerInformation().getResource());
        assertThat(request.getRequestResourceManagerInformation().getName()).isEqualTo("NEW NAME");
    }

    @Test
    void setDescription() {
        assertThat(request.getRequestResourceManagerInformation().getDescription()).isEqualTo(descr);
        request = new Request(request.getRequestResourceManagerInformation().getName(),
                request.getRequestFacultyInformation().getNetId(), "NEW DESCRIPTION",
                request.getRequestFacultyInformation().getPreferredDate(), request.getStatus(),
                request.getRequestFacultyInformation().getFaculty(),
                request.getRequestResourceManagerInformation().getResource());
        assertThat(request.getRequestResourceManagerInformation().getDescription()).isEqualTo("NEW DESCRIPTION");
    }

    @Test
    void setPreferredDate() {
        assertThat(request.getRequestFacultyInformation().getPreferredDate()).isEqualTo(date);
        LocalDate newDate = LocalDate.of(2022, 12, 6);
        request = new Request(request.getRequestResourceManagerInformation().getName(),
                request.getRequestFacultyInformation().getNetId(),
                request.getRequestResourceManagerInformation().getDescription(),
                newDate, request.getStatus(), request.getRequestFacultyInformation().getFaculty(),
                request.getRequestResourceManagerInformation().getResource());
        assertThat(request.getRequestFacultyInformation().getPreferredDate()).isEqualTo(newDate);
    }

    @Test
    void setStatus() {
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);
        request = new Request(request.getRequestResourceManagerInformation().getName(),
                request.getRequestFacultyInformation().getNetId(),
                request.getRequestResourceManagerInformation().getDescription(),
                request.getRequestFacultyInformation().getPreferredDate(),
                RequestStatus.DENIED, request.getRequestFacultyInformation().getFaculty(),
                request.getRequestResourceManagerInformation().getResource());
        assertThat(request.getStatus()).isEqualTo(RequestStatus.DENIED);
    }
}