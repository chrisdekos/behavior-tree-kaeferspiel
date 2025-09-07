package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.Direction;

/**
 * A condition node that checks if the ladybug is at the edge of the board.
 * The condition succeeds if the ladybug is at the edge of the board, fails otherwise.
 * @author ujsap
 */
public class AtEdgeNode extends ConditionNode {

    /**
     * Creates a new "at edge" condition node.
     * @param id     the node id
     * @param parent the parent node
     */
    public AtEdgeNode(String id, Node parent) {
        super(id, parent, ConditionType.AT_EDGE);
    }

    /**
     * Checks whether the ladybug is at the edge of the board.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if at the edge,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus checkCondition(TickContext tickContext) {
        return tickContext.getBoard().neighbors(
                tickContext.getLadybug().getPosition()
        ).size() < Direction.values().length
                ? NodeStatus.SUCCESS
                : NodeStatus.FAILURE;
    }
}
