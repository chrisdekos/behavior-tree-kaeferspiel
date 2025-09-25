package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;

/**
 * An action node that makes the ladybug move one step forward.
 * The move only succeeds if the cell in front is inside the board,
 * not occupied by another ladybug, and either empty or a pushable mushroom.
 * @author ujsap
 */
public class MoveNode extends ActionNode {

    /**
     * Creates a new move action node.
     * @param id the node id
     * @param parent the parent node
     */
    public MoveNode(String id, Node parent) {
        super(id, parent, ActionType.MOVE);
    }

    /**
     * Executes the move action by trying to step forward.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if the move was possible,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus executeAction(TickContext tickContext) {
        return tickContext.getLadybug().moveForward(
                tickContext.getBoard(),
                tickContext.getLadybugPositions()
        ) ? NodeStatus.SUCCESS : NodeStatus.FAILURE;
    }
}
