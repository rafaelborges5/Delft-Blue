package sem.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SysadminUserView {

    private Map<FacultyNameDTO, Resource> availableResourcesPerFaculty;
    private Map<FacultyNameDTO, Resource> reservedResourcesPerFaculty;
    private List<ClusterNodeDTO> clusterNodeDTOList;

    public SysadminUserView(
            @JsonProperty("availableResourcesPerFaculty") Map<FacultyNameDTO, Resource> availableResourcesPerFaculty,
            @JsonProperty("reservedResourcesPerFaculty") Map<FacultyNameDTO, Resource> reservedResourcesPerFaculty,
            @JsonProperty("clusterNodeDTOList") List<ClusterNodeDTO> clusterNodeDTOList) {
        this.availableResourcesPerFaculty = availableResourcesPerFaculty;
        this.reservedResourcesPerFaculty = reservedResourcesPerFaculty;
        this.clusterNodeDTOList = clusterNodeDTOList;
    }

}
