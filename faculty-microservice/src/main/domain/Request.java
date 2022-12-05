package main.domain;

import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode
public class Request {
    private String name;
    private String description;
    private Date preferredDate;


    public Request(String name, String description, Date preferredDate) {
        this.name = name;
        this.description = description;
        this.preferredDate = preferredDate;

    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preferredDate=" + preferredDate +
                '}';
    }
}
