package edu.kit.kastel.model.tree.nodes.composites;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;

/**
 * A sequence node in a behavior tree.
 * Children are executed from left to right until one fails.
 * If any child returns {@link NodeStatus#FAILURE}, this node also fails.
 * If all children succeed, this node succeeds.
 * @author ujsap
 */
public class SequenceNode extends CompositeNode {

    /**
     * Creates a new SequenceNode.
     * @param id     the node id
     * @param parent the parent node
     */
    public SequenceNode(String id, Node parent) {
        super(id, NodeType.SEQUENCE, parent);
    }

    /**
     * Executes the children in order until one fails or all succeed.
     * @param tickContext the tick context
     * @return {@link NodeStatus#FAILURE} if any child fails,
     *         otherwise {@link NodeStatus#SUCCESS}
     */
    @Override
    public NodeStatus tick(TickContext tickContext) {
        ensureEntry(tickContext);
        return runChildrenLinear(tickContext, NodeStatus.FAILURE, NodeStatus.SUCCESS);
    }
}
