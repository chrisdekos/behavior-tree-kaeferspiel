package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.view.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.InvalidArgumentException;
import edu.kit.kastel.view.Result;

/**
 * Command to return the id of the head node of a specific ladybug's behavior tree.
 * @author ujsap
 */
public class HeadCommand implements Command<Game> {
    private static final String COULD_NOT_FIND_LADYBUG_ERROR = "ladybug could not be found";

    private final int ladybugID;

    /**
     * Creates a new HeadCommand.
     * @param ladybugID the id of the ladybug whose behavior tree head is asked
     */
    public HeadCommand(int ladybugID) {
        this.ladybugID = ladybugID;
    }

    /**
     * Executes the command: finds the id of the head node of the specified ladybug's behavior tree.
     * @param handle the game instance
     * @return a {@link Result} containing the head node id,
     *         or an error result if the action cannot be performed
     */
    @Override
    public Result execute(Game handle) {
        try {
            if (!handle.allActionsEnabled()) {
                throw new AllActionsEnabledException();
            }
            if (ladybugID > handle.getLadybugsSize()) {
                throw new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR);
            }
            if (!handle.getLadybug(ladybugID).getIfActive()) {
                throw new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR);
            }
        } catch (AllActionsEnabledException | InvalidArgumentException e) {
            return Result.error(e.getMessage());
        }
        return Result.success(handle.head(ladybugID).getId());
    }
}

