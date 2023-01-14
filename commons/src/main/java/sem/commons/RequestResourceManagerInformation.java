package sem.commons;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestResourceManagerInformation {

    private String name;
    private String description;
    private Resource resource;

    /**
     * The constructor will all arguments for the RequestResourceManagerInformation.
     * @param name the name of the request
     * @param description the description of the request
     * @param resource the Resources of the requests
     */
    public RequestResourceManagerInformation(String name, String description, Resource resource) {
        this.name = name;
        this.description = description;
        this.resource = resource;
    }
}
