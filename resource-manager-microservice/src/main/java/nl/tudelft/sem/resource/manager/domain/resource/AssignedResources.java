package nl.tudelft.sem.resource.manager.domain.resource;

import lombok.NoArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.Resource;

import javax.persistence.*;
import java.util.Date;

/**
 * A DDD entity representing the number of resources allocated to a Faculty on a given Date.
 */

@Entity
@Table(name = "assigned_resources")
@NoArgsConstructor
public class AssignedResources {
    @Id
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "faculty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Assignee assignee;

    @Embedded
    private Resource resources;
}
