package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.view.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.InvalidArgumentException;
import edu.kit.kastel.view.Result;

/**
 * Command to reset the behavior tree of a specific ladybug.
 * @author ujsap
 */
public class ResetTreeCommand implements Command<Game> {
    private static final String COULD_NOT_FIND_LADYBUG_ERROR = "ladybug could not be found";
    private final int ladybugID;

    /**
     * Creates a new ResetTreeCommand.
     * @param ladybugID the id of the ladybug whose behavior tree will be reset
     */
    public ResetTreeCommand(int ladybugID) {
        this.ladybugID = ladybugID;
    }

    /**
     * Executes the command: resets the behavior tree of the specified ladybug.
     * @param handle the game instance on which to execute the command
     * @return a {@link Result} indicating success or failure
     */
    @Override
    public Result execute(Game handle) {
        try {
            if (!handle.allActionsEnabled()) {
                throw new AllActionsEnabledException();
            }
            if (!handle.getLadybug(ladybugID).getIfActive()) {
                throw new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR);
            }
        } catch (AllActionsEnabledException | InvalidArgumentException e) {
            return Result.error(e.getMessage());
        }
        handle.resetTree(ladybugID);
        return Result.success();
    }
}
