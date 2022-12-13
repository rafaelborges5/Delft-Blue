package nl.tudelft.sem.resource.manager.domain.resource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.resource.manager.domain.Resource;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * A DDD entity representing the number of resources allocated to a Faculty on a given Date.
 */

@Entity
@Table(name = "assigned_resources")
@NoArgsConstructor
@Getter
public class AssignedResources {
    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "faculty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Assignee assignee;

    @Embedded
    private Resource resources;
}
