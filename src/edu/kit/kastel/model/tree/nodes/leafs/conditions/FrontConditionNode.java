package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.CellType;

/**
 * Abstract base class for conditions that check the cell directly in front of the ladybug.
 * Subclasses define which {@link CellType} they expect to be present
 * @author ujsap
 */
public abstract class FrontConditionNode extends ConditionNode {

    /**
     * Creates a new front-cell condition node.
     * @param id            the node id
     * @param parent        the parent node
     * @param conditionType the type of the condition
     */
    public FrontConditionNode(String id, Node parent, ConditionType conditionType) {
        super(id, parent, conditionType);
    }

    /**
     * Checks if the cell in front of the ladybug matches the expected type.
     * @param tickContext the tick context with board and ladybug state
     * @param expected    the expected cell type in front
     * @return {@link NodeStatus#SUCCESS} if the cell matches,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    protected final NodeStatus checkFrontCell(TickContext tickContext, CellType expected) {
        if (!tickContext.getBoard().isWithinBounds(tickContext.getLadybug().getCellInFront())) {
            return NodeStatus.FAILURE;
        }
        return tickContext.getBoard().getCellType(tickContext.getLadybug().getCellInFront()) == expected
                ? NodeStatus.SUCCESS
                : NodeStatus.FAILURE;
    }
}
