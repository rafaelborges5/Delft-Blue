package domain;

import java.time.LocalDate;
import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "requests")
@EqualsAndHashCode
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

    //Resources here!

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
}
