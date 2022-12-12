package nl.tudelft.sem.template.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PendingRequestsDTO {
    private String status;
    private List<RequestDTO> requests;
}
