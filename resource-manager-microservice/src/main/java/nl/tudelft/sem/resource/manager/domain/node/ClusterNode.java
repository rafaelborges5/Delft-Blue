package nl.tudelft.sem.resource.manager.domain.node;

import lombok.*;
import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.node.converters.OwnerNameConverter;
import nl.tudelft.sem.resource.manager.domain.node.converters.TokenConverter;
import nl.tudelft.sem.resource.manager.domain.node.converters.URLConverter;

import javax.persistence.*;
import java.util.Objects;


/**
 * A DDD entity representing a DelftBlue cluster node.
 */

@Entity
@Table(name = "cluster_nodes")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClusterNode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "token", nullable = false, unique = true)
    @Convert(converter = TokenConverter.class)
    private Token token;

    @Column(name = "owner_name", nullable = false, unique = false)
    @Convert(converter = OwnerNameConverter.class)
    private OwnerName ownerName;

    @Column(name = "url", nullable = false, unique = true)
    @Convert(converter = URLConverter.class)
    private URL url;

    @Embedded
    private Resource resources;

    /**
     * Basic constructor.
     *
     * @param ownerName the name of the node owner
     * @param url the url of the node
     * @param token the token needed to access the node
     * @param resources the amount of resources provided by the node
     */
    public ClusterNode(OwnerName ownerName, URL url, Token token, Resource resources) {
        this.ownerName = ownerName;
        this.url = url;
        this.token = token;
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClusterNode clusterNode = (ClusterNode) o;
        return token == (clusterNode.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
