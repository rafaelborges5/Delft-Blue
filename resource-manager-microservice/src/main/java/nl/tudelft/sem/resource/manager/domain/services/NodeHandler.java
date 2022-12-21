package nl.tudelft.sem.resource.manager.domain.services;

import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;
import nl.tudelft.sem.resource.manager.domain.node.NodeRepository;
import nl.tudelft.sem.resource.manager.domain.node.exceptions.NodeNotFoundException;
import org.springframework.stereotype.Service;
import sem.commons.Token;

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
     * This method will add a new node to the system
     * @param node the node to add to the system
     * @return a string representing weather the addition was successful or not.
     */
    public String addNodeToCluster(ClusterNode node) {
        return ("NotImplemented");
    }

    /**
     * Finds a node by the token and removes it from the cluster.
     * @param token by which to find the node
     * @throws NodeNotFoundException if there is no node with that token in the cluster
     */
    public void removeNodeFromCluster(Token token) throws NodeNotFoundException {

    }
}
