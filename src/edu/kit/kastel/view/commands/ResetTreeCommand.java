package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.view.exceptions.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.exceptions.InvalidArgumentException;
import edu.kit.kastel.view.Result;

/**
 * Command to reset the behavior tree of a specific ladybug.
 * @author ujsap
 */
public class ResetTreeCommand implements Command<Game> {
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
        if (handle.areActionsBlocked()) {
            return Result.error(new AllActionsEnabledException().getMessage());
        }
        if (!handle.getLadybug(ladybugID).getIfActive()) {
            return Result.error(new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR).getMessage());
        }
        handle.resetTree(ladybugID);
        return Result.success();
    }
}
