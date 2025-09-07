package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;

/**
 * An action node that makes the ladybug turn to the left.
 * This action always succeeds.
 * @author ujsap
 */
public class TurnLeftNode extends ActionNode {

    /**
     * Creates a new turn-left action node.
     * @param id     the node id
     * @param parent the parent node
     */
    public TurnLeftNode(String id, Node parent) {
        super(id, parent, ActionType.TURN_LEFT);
    }

    /**
     * Executes the turn-left action.
     * @param tickContext the tick context with board and ladybug state
     * @return always {@link NodeStatus#SUCCESS}
     */
    @Override
    protected NodeStatus executeAction(TickContext tickContext) {
        tickContext.getLadybug().turnLeft();
        return NodeStatus.SUCCESS;
    }
}
