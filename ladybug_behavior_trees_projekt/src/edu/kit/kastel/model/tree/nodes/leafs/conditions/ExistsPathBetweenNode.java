package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.Position;

/**
 * A condition node that checks if there exists a path between two positions on the board.
 * The condition succeeds if the board reports a valid path from the
 * start position to the goal position.
 * @author ujsap
 */
public class ExistsPathBetweenNode extends ConditionNode {
    private static final String POSITION_FORMAT = " %d,%d";
    private final Position goal;
    private final Position start;

    /**
     * Creates a new "exists path between" condition node.
     * @param id     the node id
     * @param parent the parent node
     * @param start  the start position
     * @param goal   the goal position
     */
    public ExistsPathBetweenNode(String id, Node parent, Position start, Position goal) {
        super(id, parent, ConditionType.EXISTS_PATH_BETWEEN);
        this.goal = goal;
        this.start = start;
    }

    /**
     * Checks if a path exists between the start and goal positions.
     * @param tickContext the tick context with the board state
     * @return {@link NodeStatus#SUCCESS} if a path exists,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus executeCondition(TickContext tickContext) {
        return tickContext.getBoard().existsPath(start, goal)
                ? NodeStatus.SUCCESS
                : NodeStatus.FAILURE;
    }

    @Override
    public String getSpecificType() {
        return super.getSpecificType() + POSITION_FORMAT.formatted(start.column(), start.row())
                + POSITION_FORMAT.formatted(goal.column(), goal.row());
    }

}
