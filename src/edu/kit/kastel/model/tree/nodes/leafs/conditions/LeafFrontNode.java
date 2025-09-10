package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.CellType;

/**
 * A condition node that checks if there is a leaf in front of the ladybug.
 * The condition succeeds if the cell directly in front exists and contains a {@link CellType#LEAF}.
 * @author ujsap
 */
public class LeafFrontNode extends FrontConditionNode {

    /**
     * Creates a new LeafFront condition node.
     * @param id     the node id
     * @param parent the parent node
     */
    public LeafFrontNode(String id, Node parent) {
        super(id, parent, ConditionType.LEAF_FRONT);
    }

    /**
     * Checks whether the cell in front of the ladybug contains a leaf.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if a leaf is in front,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus executeCondition(TickContext tickContext) {
        return executeFrontCell(tickContext, CellType.LEAF);
    }
}
