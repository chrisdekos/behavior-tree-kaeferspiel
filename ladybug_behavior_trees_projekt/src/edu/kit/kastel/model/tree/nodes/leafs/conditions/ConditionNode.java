package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;
import edu.kit.kastel.model.tree.nodes.leafs.LeafNode;

/**
 * Base class for all condition nodes in a behavior tree.
 * A condition node checks a specific state on the board.
 * Subclasses implement the concrete check in {@link #executeCondition(TickContext)}.
 * @author ujasp
 */
public abstract class ConditionNode extends LeafNode {
    private final ConditionType conditionType;

    /**
     * Creates a new condition node.
     * @param id            the node id
     * @param parent        the parent node
     * @param conditionType the specific condition type
     */
    protected ConditionNode(String id, Node parent, ConditionType conditionType) {
        super(id, NodeType.CONDITION, parent);
        this.conditionType = conditionType;
    }

    private ConditionType getConditionType() {
        return this.conditionType;
    }

    /**
     * Returns the string representation of this node's condition type.
     * @return the condition type as string
     */
    @Override
    public String getSpecificType() {
        return getConditionType().toString();
    }

    /**
     * Evaluates this condition by calling {@link #executeCondition(TickContext)}.
     * @param tickContext the context for this tick
     * @return the result of the condition check
     */
    @Override
    protected NodeStatus evaluate(TickContext tickContext) {
        return executeCondition(tickContext);
    }

    /**
     * Subclasses implement their condition logic here.
     * @param tickContext the context for this tick
     * @return {@link NodeStatus#SUCCESS} if the condition is true,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    protected abstract NodeStatus executeCondition(TickContext tickContext);
}
