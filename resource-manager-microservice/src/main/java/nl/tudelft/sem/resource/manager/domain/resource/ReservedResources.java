package nl.tudelft.sem.resource.manager.domain.resource;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class ReservedResources {
    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "reserver", nullable = false)
    @Enumerated(EnumType.STRING)
    private Reserver reserver;

    @Embedded
    private Resource resources;

    /**
     * Basic constructor.
     *
     * @param date the date for which the resources are reserved
     * @param reserver the entity that reserved the resources
     * @param resources the amount of resources that were reserved
     */
    public ReservedResources(LocalDate date, Reserver reserver, Resource resources) {
        this.date = date;
        this.reserver = reserver;
        this.resources = resources;
    }
}
