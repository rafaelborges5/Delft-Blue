package sem.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExampleUser {
    String netId;

    public ExampleUser(@JsonProperty("netId") String netId) {
        this.netId = netId;
    }
}