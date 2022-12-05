package main.domain;

import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode
public class Request {
    private String name;
    private String description;
    private Date preferredDate;
    private Status status;

    public Request(String name, String description, Date preferredDate, Status status) {
        this.name = name;
            this.description = description;
        this.preferredDate = preferredDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preferredDate=" + preferredDate +
                ", status=" + status +
                '}';
    }
}
