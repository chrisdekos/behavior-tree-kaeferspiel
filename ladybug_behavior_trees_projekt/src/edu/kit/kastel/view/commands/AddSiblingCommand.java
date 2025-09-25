package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.exceptions.TreeParserException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.exceptions.AllActionsEnabledException;
import edu.kit.kastel.view.exceptions.InvalidArgumentException;

/**
 * Command to add a new sibling node in a ladybug's behavior tree.
 * @author ujsap
 */
public class AddSiblingCommand implements Command<Game> {
    private static final String ROOT_CAN_NOT_HAVE_A_SIBLING_ERROR = "root can not have a sibling";
    private final int ladybugID;
    private final String existingNodeID;
    private final String newNode;

    /**
     * Creates a new AddSiblingCommand.
     * @param ladybugID       the id of the ladybug whose tree will be modified
     * @param existingNodeID  the id of the node after which the new node is inserted
     * @param newNode         the textual representation of the new node
     */
    public AddSiblingCommand(int ladybugID, String existingNodeID, String newNode) {
        this.ladybugID = ladybugID;
        this.existingNodeID = existingNodeID;
        this.newNode = newNode;
    }

    /**
     * Executes the command: inserts a new sibling node into the tree.
     * @param handle the game instance on which to execute the command
     * @return a {@link Result} indicating success or failure
     */
    @Override
    public Result execute(Game handle) {
        if (handle.areActionsBlocked()) {
            return Result.error(new AllActionsEnabledException().getMessage());
        }

        if (!handle.getLadybug(ladybugID).getIfActive()) {
            return Result.error(new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR).getMessage());
        }

        if (existingNodeID.equals(handle.getLadybug(ladybugID).getBehaviorTree().getRootID())) {
            return Result.error(new InvalidArgumentException(ROOT_CAN_NOT_HAVE_A_SIBLING_ERROR).getMessage());
        }

        if (!handle.getLadybug(ladybugID).getBehaviorTree().hasNode(existingNodeID)) {
            return Result.error(new InvalidArgumentException(COULD_NOT_FIND_NODE_ERROR).getMessage());
        }
        try {
            handle.addSibling(ladybugID, existingNodeID, newNode);
        } catch (TreeParserException e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }
}
