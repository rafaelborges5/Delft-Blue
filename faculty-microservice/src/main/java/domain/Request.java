package domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests")
@NoArgsConstructor
@EqualsAndHashCode
public class Request {
    @Id
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "preferredDate", nullable = false)
    private LocalDate preferredDate;
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Constructor method.
     */
    public Request(String name, String description, LocalDate preferredDate, Status status) {
        this.name = name;
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
                + "name='" + name + '\''
                + ", description='" + description + '\''
                + ", preferredDate=" + preferredDate
                + ", status=" + status
                + '}';
    }

    /**
     * Getter for name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for preferred date.
     */
    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    /**
     * Getter for status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Setter for status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
