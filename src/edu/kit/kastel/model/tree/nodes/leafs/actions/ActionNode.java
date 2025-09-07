package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;
import edu.kit.kastel.model.tree.nodes.leafs.LeafNode;

/**
 * Base class for all action nodes in a behavior tree.
 * An action node performs a concrete step when evaluated.
 * Subclasses implement the specific action in executeAction.
 * @author ujsap
 */
public abstract class ActionNode extends LeafNode {
    private final ActionType actionType;

    /**
     * Creates a new action node.
     * @param id         the node id
     * @param parent     the parent node
     * @param actionType the specific action type
     */
    public ActionNode(String id, Node parent, ActionType actionType) {
        super(id, NodeType.ACTION, parent);
        this.actionType = actionType;
    }

    /**
     * Gets the type of this action
     * @return the type of this action
     */
    protected ActionType getActionType() {
        return this.actionType;
    }

    /**
     * Returns the string representation of this node's action type.
     * @return the action type as string
     */
    @Override
    public String getSpecificType() {
        return getActionType().toString();
    }

    /**
     * Executes the concrete action.
     * Subclasses must implement this method with the logic for their action.
     * @param tickContext the context for this tick
     * @return the status of the action after execution
     */
    protected abstract NodeStatus executeAction(TickContext tickContext);

    /**
     * Evaluates this node by executing its action.
     * @param tickContext the context for this tick
     * @return the result of the action
     */
    @Override
    protected NodeStatus evaluate(TickContext tickContext) {
        return executeAction(tickContext);
    }
}
