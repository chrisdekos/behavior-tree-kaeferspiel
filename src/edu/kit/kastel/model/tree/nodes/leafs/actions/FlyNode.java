package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.Position;

/**
 * An action node that makes the ladybug fly directly to a target position.
 * The move only succeeds if the target cell is inside the board, empty,
 * and not already occupied by another ladybug.
 * @author ujsap
 */
public class FlyNode extends ActionNode {
    private static final String GOAL_POSITION_FORMAT = " %d,%d";
    private final Position goal;

    /**
     * Creates a new fly action node.
     * @param id     the node id
     * @param parent the parent node
     * @param goal   the target position to fly to
     */
    public FlyNode(String id, Node parent, Position goal) {
        super(id, parent, ActionType.FLY);
        this.goal = goal;
    }

    /**
     * Executes the fly action by moving the ladybug to the goal position if possible
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if the ladybug could fly,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus executeAction(TickContext tickContext) {
        return tickContext.getLadybug().fly(
                tickContext.getBoard(),
                goal,
                tickContext.getLadybugPositions()
        ) ? NodeStatus.SUCCESS : NodeStatus.FAILURE;
    }

    @Override
    public String getSpecificType() {
        return super.getSpecificType() + GOAL_POSITION_FORMAT.formatted(goal.column(), goal.row());
    }
}
