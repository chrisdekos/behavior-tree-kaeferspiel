package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.view.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.InvalidArgumentException;
import edu.kit.kastel.view.Result;

/**
 * Command to jump a ladybug's behavior tree current node to a specific node.
 * @author ujsap
 */
public class JumpToCommand implements Command<Game> {
    private static final String COULD_NOT_FIND_LADYBUG_ERROR = "ladybug could not be found";

    private final int ladybugID;
    private final String nodeID;

    /**
     * Creates a new JumpToCommand.
     * @param ladybugID the id of the ladybug whose tree should jump
     * @param nodeID    the target node id to jump to
     */
    public JumpToCommand(int ladybugID, String nodeID) {
        this.ladybugID = ladybugID;
        this.nodeID = nodeID;
    }

    /**
     * Executes the command: jumps the specified ladybug's behavior tree to the given node.
     * @param handle the game instance
     * @return a {@link Result} indicating success,
     *         or an error if the action cannot be performed
     */
    @Override
    public Result execute(Game handle) {
        try {
            if (!handle.allActionsEnabled()) {
                throw new AllActionsEnabledException();
            }
            if (handle.getLadybug(ladybugID).getIfActive()) {
                throw new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR);
            }
        } catch (AllActionsEnabledException | InvalidArgumentException e) {
            return Result.error(e.getMessage());
        }

        handle.jumpTo(ladybugID, nodeID);
        return Result.success();
    }
}
