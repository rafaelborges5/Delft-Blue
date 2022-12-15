package sem.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AcceptRequestsDTO {

    private String facultyName;
    private List<RequestDTO> acceptedRequests;

    public AcceptRequestsDTO(
            @JsonProperty("facultyName") String facultyName,
            @JsonProperty("requests") List<RequestDTO> acceptedRequests
    ) {
        this.facultyName = facultyName;
        this.acceptedRequests = acceptedRequests;
    }
}
