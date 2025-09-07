package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.board.CellType;

/**
 * A condition node that checks if there is a tree in front of the ladybug.
 * The condition succeeds if the cell directly in front exists and contains a {@link CellType#TREE}.
 * @author ujsap
 */
public class TreeFrontNode extends FrontConditionNode {

    /**
     * Creates a new TreeFront condition node.
     * @param id     the node id
     * @param parent the parent node
     */
    public TreeFrontNode(String id, Node parent) {
        super(id, parent, ConditionType.TREE_FRONT);
    }

    /**
     * Checks whether the cell in front of the ladybug contains a tree.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if a tree is in front,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus checkCondition(TickContext tickContext) {
        return checkFrontCell(tickContext, CellType.TREE);
    }
}
