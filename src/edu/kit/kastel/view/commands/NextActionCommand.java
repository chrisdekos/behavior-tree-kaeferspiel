package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.tree.TraceEntry;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.view.exceptions.AllActionsEnabledException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.util.PrintHelpers;
import edu.kit.kastel.view.util.TraceEntriesPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Command to perform the next action for all active ladybugs.
 * It prints the resulting trace entries and the updated board.
 * @author Programmieren-Team
 * @author ujsap
 */
public class NextActionCommand implements Command<Game> {

    /**
     * Executes the command: triggers the next action of all active ladybugs,
     * collects their trace entries, and appends a rendered board state.
     * @param handle the game instance
     * @return a {@link Result} containing the trace and rendered board,
     *         or an error result if actions are not enabled
     */
    @Override
    public Result execute(Game handle) {

        if (handle.areActionsBlocked()) {
            return Result.error(new AllActionsEnabledException().getMessage());
        }

        List<Ladybug> ladybugsToTick = new ArrayList<>();
        for (Ladybug ladybug : handle.listLadybugs()) {
            if (ladybug.getIfActive()) {
                ladybugsToTick.add(ladybug);
            }
        }
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        for (Ladybug ladybug : ladybugsToTick) {
            for (TraceEntry traceEntry : handle.singleNextAction(ladybug.getId()).getEntries()) {
                joiner.add(TraceEntriesPrinter.format(traceEntry));
            }
            joiner.add(PrintHelpers.prepareRenderedBoard(handle.getBoard(), ladybugsToTick));
        }
        return Result.success(joiner.toString());
    }
}
