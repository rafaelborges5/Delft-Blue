package domain;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long requestId;
    private String netId;
    private String name;
    private String description;
    private LocalDate preferredDate;
    private RequestStatus status;

    //TODO: Resources here!

    /**
     * Empty Constructor.
     */
    public Request() {
    }

    /**
     * Constructor method.
     */
    public Request(String name, String netId, String description, LocalDate preferredDate, RequestStatus status) {
        this.name = name;
        this.netId = netId;
        this.description = description;
        this.preferredDate = preferredDate;
        this.status = status;
    }

    /**
     * toString method.
     */
    @Override
    public String toString() {
        return "Request{"
                + "requestId='" + requestId + '\''
                + ", netId='" + netId + '\''
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", preferredDate=" + preferredDate
                + ", status=" + status
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
                && status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, netId, name, description, preferredDate, status);
    }
}
