package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.exceptions.TreeParserException;
import edu.kit.kastel.view.BoardNotLoadedException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.InvalidArgumentException;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.util.FilesReader;
import edu.kit.kastel.view.util.PrintHelpers;

import java.util.List;

/**
 * Command to load behavior trees into the game.
 * The trees are read from one or more input files and
 * then assigned to the existing ladybugs on the board.
 * @author ujsap
 */
public class LoadTreesCommand implements Command<Game>  {
    private final List<String> inputLines;

    /**
     * Creates a new LoadTreesCommand and merges all tree files into input lines.
     * @param files the list of file paths containing tree definitions
     * @throws InvalidArgumentException if any file cannot be read
     */
    public LoadTreesCommand(List<String> files) throws InvalidArgumentException {
        this.inputLines = FilesReader.mergeTreeFiles(files);
    }

    /**
     * Executes the command: loads all parsed trees into the game.
     * @param handle the game instance
     * @return a {@link Result} containing the verbatim tree definitions on success,
     *         or an error result if the parsing fails
     */
    @Override
    public Result execute(Game handle) {
        System.out.println(PrintHelpers.prepareVerbatimPrint(inputLines));
        try {
            if (!handle.isBoardLoaded()) {
                throw new BoardNotLoadedException();
            }
            handle.loadTrees(inputLines);
        } catch (TreeParserException | BoardNotLoadedException e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }
}
