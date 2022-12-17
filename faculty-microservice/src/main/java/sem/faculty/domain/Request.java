package sem.faculty.domain;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sem.commons.FacultyName;
import sem.commons.Resource;

/**
 * A DDD entity representing an request in our domain.
 */
@Entity
@Table(name = "requests")
@NoArgsConstructor
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "requestId", nullable = false)
    private long requestId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "netId", nullable = false)
    private String netId;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "preferredDate", nullable = false)
    private LocalDate preferredDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "facultyName", nullable = false)
    private FacultyName facultyName;
    @Embedded
    private Resource resource;

    /**
     * Constructor method.
     */
    public Request(String name, String netId, String description,
                   LocalDate preferredDate, RequestStatus status, FacultyName facultyName, Resource resource) {
        this.name = name;
        this.netId = netId;
        this.description = description;
        this.preferredDate = preferredDate;
        this.status = status;
        this.facultyName = facultyName;
        this.resource = resource;
    }

    /**
     * toString method.
     */
    @Override
    public String toString() {
        return "Request{"
                + "requestId=" + requestId
                + ", netId='" + netId + '\''
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", preferredDate=" + preferredDate
                + ", status=" + status
                + ", faculty=" + facultyName
                + ", resource='" + resource.toString() + '\''
                + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }
        Request request = (Request) o;
        return requestId == request.requestId
                && Objects.equals(netId, request.netId)
                && Objects.equals(name, request.name)
                && Objects.equals(description, request.description)
                && Objects.equals(preferredDate, request.preferredDate)
                && status == request.status
                && facultyName == request.facultyName
                && Objects.equals(resource, request.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, netId, name, description, preferredDate, status, facultyName, resource);
    }
}
