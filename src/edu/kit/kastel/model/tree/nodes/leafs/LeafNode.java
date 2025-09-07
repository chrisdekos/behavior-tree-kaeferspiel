package edu.kit.kastel.model.tree.nodes.leafs;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;

import java.util.Map;

/**
 * Base class for all leaf nodes in a behavior tree.
 * A leaf node has no children and directly produces a {@link NodeStatus} when evaluated.
 * @author ujsap
 */
public abstract class LeafNode extends Node {

    /**
     * Creates a new leaf node.
     * @param id       the node id
     * @param nodeType the type of this node
     * @param parent   the parent node
     */
    protected LeafNode(String id, NodeType nodeType, Node parent) {
        super(id, nodeType, parent);
    }

    /**
     * Executes the actual evaluation logic of the leaf.
     * @param tickContext the context for this tick
     * @return the evaluation result
     */
    protected abstract NodeStatus evaluate(TickContext tickContext);

    /**
     * Executes this node for one tick.
     * Calls {@link #evaluate(TickContext)}, stores the result as the node's status, and logs the outcome.
     * @param tickContext the context for this tick
     * @return the resulting node status
     */
    @Override
    public final NodeStatus tick(TickContext tickContext) {
        NodeStatus result = evaluate(tickContext);
        setNodeStatus(result);
        logExit(tickContext, result);
        return result;
    }

    /**
     * Registers this leaf node in the given node index.
     * @param index the map of node ids to nodes
     */
    @Override
    public void registerSubtree(Map<String, Node> index) {
        index.put(getId(), this);
    }
}
