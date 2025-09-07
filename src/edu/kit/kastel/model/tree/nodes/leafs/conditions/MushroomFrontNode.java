package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.CellType;

/**
 * A condition node that checks if there is a mushroom in front of the ladybug.
 * The condition succeeds if the cell directly in front exists and contains a {@link CellType#MUSHROOM}.
 * @author ujsap
 */
public class MushroomFrontNode extends FrontConditionNode {

    /**
     * Creates a new MushroomFront condition node.
     * @param id     the node id
     * @param parent the parent node
     */
    public MushroomFrontNode(String id, Node parent) {
        super(id, parent, ConditionType.MUSHROOM_FRONT);
    }

    /**
     * Checks whether the cell in front of the ladybug contains a mushroom.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if a mushroom is in front,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus checkCondition(TickContext tickContext) {
        return checkFrontCell(tickContext, CellType.MUSHROOM);
    }
}
