package domain;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Request {
    private String name;
    private String description;
    private LocalDate preferredDate;
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
