package edu.kit.kastel.model.tree.nodes.composites;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;

/**
 * A fallback node in a behavior tree.
 * Children are executed from left to right until one succeeds.
 * If any child returns {@link NodeStatus#SUCCESS}, this node also succeeds.
 * If all children fail, this node fails.
 * @author ujsap
 */
public class FallbackNode extends CompositeNode {
    /**
     * Creates a new FallbackNode.
     * @param id     the node id
     * @param parent the parent node
     */
    public FallbackNode(String id, Node parent) {
        super(id, NodeType.FALLBACK, parent);
    }

    /**
     * Executes the children in order until one succeeds or all fail.
     * @param tickContext the tick context
     * @return {@link NodeStatus#SUCCESS} if any child succeeds,
     *         otherwise {@link NodeStatus#FAILURE}
     */
    @Override
    public NodeStatus tick(TickContext tickContext) {
        ensureEntry(tickContext);
        return runChildrenLinear(tickContext, NodeStatus.SUCCESS, NodeStatus.FAILURE);
    }
}
