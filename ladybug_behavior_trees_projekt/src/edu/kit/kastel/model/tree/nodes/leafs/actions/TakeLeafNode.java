package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;

/**
 * An action node that makes the ladybug take a leaf from the cell in front.
 * The action only succeeds if the cell in front is inside the board and currently contains a leaf.
 * @author ujsap
 **/
public class TakeLeafNode extends ActionNode {

    /**
     * Creates a new take-leaf action node.
     * @param id     the node id
     * @param parent the parent node
     */
    public TakeLeafNode(String id, Node parent) {
        super(id, parent, ActionType.TAKE_LEAF);
    }

    /**
     * Executes the take-leaf action.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if a leaf was taken,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus executeAction(TickContext tickContext) {
        return tickContext.getLadybug().takeLeafFront(
                tickContext.getBoard()
        ) ? NodeStatus.SUCCESS : NodeStatus.FAILURE;
    }
}
