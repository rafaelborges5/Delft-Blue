package nl.tudelft.sem.notification.manager.domain.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * A DDD entity representing user Notification
 */
@Entity
@Table(name = "notifications")
@NoArgsConstructor
public class Notification {

    /**
     * The identifier for the Notification
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ownerNetId", nullable = false, unique = false)
    @Convert(converter = String.class)
    private String ownerNetId;

    @Column(name = "description", nullable = false, unique = false)
    @Convert(converter = String.class)
    private String description;

    @Column(name = "seen", nullable = false, updatable = false)
    @Convert(converter = Boolean.class)
    private boolean seen;
}
