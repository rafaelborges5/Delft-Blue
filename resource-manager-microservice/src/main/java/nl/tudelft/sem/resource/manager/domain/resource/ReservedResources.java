package nl.tudelft.sem.resource.manager.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.Resource;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * A DDD entity representing the number of resources allocated to a Faculty on a given Date.
 */

@Entity
@Table(name = "reserved_resources")
@NoArgsConstructor
@AllArgsConstructor
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
}
