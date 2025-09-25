package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.board.Position;
import edu.kit.kastel.view.exceptions.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.exceptions.InvalidArgumentException;
import edu.kit.kastel.view.Result;

import static edu.kit.kastel.view.util.PrintHelpers.toViewPosition;

/**
 * Command to print the current position of a specific ladybug.
 * @author ujsap
 */
public class PrintPositionCommand implements Command<Game> {
    private final int ladybugID;

    /**
     * Creates a new PrintPositionCommand.
     * @param ladybugID the id of the ladybug whose position will be printed
     */
    public PrintPositionCommand(int ladybugID) {
        this.ladybugID = ladybugID;
    }

    /**
     * Executes the command: prints the position of the specified ladybug.
     * @param handle the game instance
     * @return a {@link Result} with the position of the ladybug,
     *         or an error result if the action cannot be executed
     */
    @Override
    public Result execute(Game handle) {
        if (handle.areActionsBlocked()) {
            return Result.error(new AllActionsEnabledException().getMessage());
        }
        if (!handle.getLadybug(ladybugID).getIfActive()) {
            return Result.error(new InvalidArgumentException(COULD_NOT_FIND_LADYBUG_ERROR).getMessage());
        }
        Position position = handle.getLadybug(ladybugID).getPosition();
        return Result.success(toViewPosition(position));
    }
}
