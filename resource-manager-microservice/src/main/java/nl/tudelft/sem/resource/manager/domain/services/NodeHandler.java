package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.node.Token;
import nl.tudelft.sem.resource.manager.domain.node.exceptions.NodeNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service that handles adding and removing ClusterNodes from the cluster.
 */
@Service
public class NodeHandler {
    private transient NodeRepository nodeRepository;

    public NodeHandler(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    /**
     * Adds the node to the cluster.
     * @param node the node to add to the cluster
     */
    public void addNodeToCluster(ClusterNode node) {

    }

    /**
     * Finds a node by the token and removes it from the cluster.
     * @param token by which to find the node
     * @throws NodeNotFoundException if there is no node with that token in the cluster
     */
    public void removeNodeFromCluster(Token token) throws NodeNotFoundException {

    }
}
