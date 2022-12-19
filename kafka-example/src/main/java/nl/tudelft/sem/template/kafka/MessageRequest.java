package nl.tudelft.sem.template.kafka;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageRequest {

    private String message;

    public MessageRequest(@JsonProperty("message") String message) {
        this.message = message;
    }
}
