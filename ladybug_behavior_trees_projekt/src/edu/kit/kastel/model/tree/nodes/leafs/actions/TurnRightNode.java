package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;

/**
 * An action node that makes the ladybug turn to the right.
 * This action always succeeds.
 * @author ujsap
 */
public class TurnRightNode extends ActionNode {

    /**
     * Creates a new turn-right action node.
     * @param id     the node id
     * @param parent the parent node (may be {@code null} if root)
     */
    public TurnRightNode(String id, Node parent) {
        super(id, parent, ActionType.TURN_RIGHT);
    }

    /**
     * Executes the turn-right action.
     * @param tickContext the tick context with board and ladybug state
     * @return always {@link NodeStatus#SUCCESS}
     */
    @Override
    protected NodeStatus executeAction(TickContext tickContext) {
        tickContext.getLadybug().turnRight();
        return NodeStatus.SUCCESS;
    }
}
