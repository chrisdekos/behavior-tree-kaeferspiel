package edu.kit.kastel.model.tree.nodes.leafs.actions;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;

/**
 * An action node that makes the ladybug place a leaf in the cell in front.
 * The action only succeeds if the cell in front is inside the board and empty.
 * @author ujsap
 */
public class PlaceLeafNode extends ActionNode {

    /**
     * Creates a new place-leaf action node.
     * @param id     the node id
     * @param parent the parent node
     */
    public PlaceLeafNode(String id, Node parent) {
        super(id, parent, ActionType.PLACE_LEAF);
    }

    /**
     * Executes the place-leaf action.
     * @param tickContext the tick context with board and ladybug state
     * @return {@link NodeStatus#SUCCESS} if a leaf was placed,
     *         {@link NodeStatus#FAILURE} otherwise
     */
    @Override
    protected NodeStatus executeAction(TickContext tickContext) {
        return tickContext.getLadybug().placeLeafFront(
                tickContext.getBoard()
        ) ? NodeStatus.SUCCESS : NodeStatus.FAILURE;
    }
}
