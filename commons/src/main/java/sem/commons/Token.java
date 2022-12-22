package sem.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * A DTO representing the Token required to access the Node.
 */

@Data
public class Token {
    public final transient String tokenValue;

    public Token(
            @JsonProperty("tokenValue") String tokenValue
    ) {
        this.tokenValue = tokenValue;
    }
}
