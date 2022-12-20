package nl.tudelft.sem.resource.manager.domain.node;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A DDD Repository for querying information about the cluster Nodes.
 */

@Repository
public interface NodeRepository extends JpaRepository<ClusterNode, Long> {
}
