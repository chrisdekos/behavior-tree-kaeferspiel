package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.Position;

/**
 * A condition node that checks if there exists a path
 * from the ladybug's current position to a given goal position.
 * The condition succeeds if the board reports a valid path from the ladybug to the goal.
 * @author ujsap
 */
public class ExistsPathToNode extends ConditionNode {
    private static final String GOAL_POSITION_FORMAT = " %d,%d";
    private final Position goal;

    /**
     * Creates a new "exists path to" condition node.
     * @param id     the node id
     * @param parent the parent node
     * @param goal   the goal position
     */
    public ExistsPathToNode(String id, Node parent, Position goal) {
        super(id, parent, ConditionType.EXISTS_PATH_TO);
        this.goal = goal;
    }

    /**
     * Checks if a path exists from the ladybug's current position to the goal.
     * @param tickContext the tick context with the board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if a path exists,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus checkCondition(TickContext tickContext) {
        return tickContext.getBoard().existsPath(
                tickContext.getLadybug().getPosition(),
                goal
        ) ? NodeStatus.SUCCESS : NodeStatus.FAILURE;
    }

    @Override
    public String getSpecificType() {
        return super.getSpecificType() + GOAL_POSITION_FORMAT.formatted(goal.column(), goal.row());
    }
}
