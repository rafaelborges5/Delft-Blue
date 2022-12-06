package nl.tudelft.sem.template.authentication.kafkaconfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Example user.
 */
@Data
public class ExampleUser {
    /**
     * The Net id.
     */
    String netId;

    /**
     * Instantiates a new Example user.
     *
     * @param netId the net id
     */
    public ExampleUser(@JsonProperty("netId") String netId) {
        this.netId = netId;
    }
}
