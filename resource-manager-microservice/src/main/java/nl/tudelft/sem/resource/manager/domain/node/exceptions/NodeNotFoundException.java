package nl.tudelft.sem.resource.manager.domain.node.exceptions;

import nl.tudelft.sem.resource.manager.domain.node.ClusterNode;

public class NodeNotFoundException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public NodeNotFoundException(ClusterNode node) {
        super(node.toString());
    }
}
