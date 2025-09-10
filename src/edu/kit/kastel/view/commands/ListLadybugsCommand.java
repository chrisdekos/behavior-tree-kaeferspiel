package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.view.exceptions.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.Result;

import java.util.StringJoiner;

/**
 * Command to list all active ladybugs in the current game.
 * Only ladybugs marked as active are included in the output.
 * The result is a whitespace-separated list of ladybug IDs.
 * @author ujsap
 */
public class ListLadybugsCommand implements Command<Game> {
    private static final String WHITESPACE = " ";

    /**
     * Executes the command: lists the IDs of all active ladybugs.
     * @param handle the game instance
     * @return a {@link Result} containing the list of active ladybug IDs,
     *         or an error result if actions are not enabled
     */
    @Override
    public Result execute(Game handle) {
        if (handle.areActionsBlocked()) {
            return Result.error(new AllActionsEnabledException().getMessage());
        }
        StringJoiner stringJoiner = new StringJoiner(WHITESPACE);
        for (Ladybug ladybug : handle.listLadybugs()) {
            if (!ladybug.getIfActive()) {
                continue;
            }
            stringJoiner.add(String.valueOf(ladybug.getId()));
        }
        return Result.success(stringJoiner.toString());
    }
}
