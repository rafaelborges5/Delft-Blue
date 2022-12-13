package nl.tudelft.sem.resource.manager.domain.node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.converters.OwnerNameConverter;
import nl.tudelft.sem.resource.manager.domain.node.converters.TokenConverter;
import nl.tudelft.sem.resource.manager.domain.node.converters.URLConverter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.GenerationType;
import java.util.Objects;


/**
 * A DDD entity representing a DelftBlue cluster node.
 */

@Entity
@Table(name = "cluster_nodes")
@NoArgsConstructor
@Getter
public class ClusterNode {
    /**
     * Identifier for the node.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "owner_name", nullable = false, unique = false)
    @Convert(converter = OwnerNameConverter.class)
    private OwnerName ownerName;

    @Column(name = "url", nullable = false, unique = true)
    @Convert(converter = URLConverter.class)
    private URL url;

    @Column(name = "token", nullable = true, unique = true)
    @Convert(converter = TokenConverter.class)
    private Token token;

    @Embedded
    private Resource resources;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClusterNode clusterNode = (ClusterNode) o;
        return id == (clusterNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
