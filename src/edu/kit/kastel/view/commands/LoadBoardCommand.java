package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.exceptions.BoardParserException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.InvalidArgumentException;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.util.FilesReader;
import edu.kit.kastel.view.util.PrintHelpers;

import java.util.List;

/**
 * Command to load a board into the game.
 * The board is read from a given input file and then initialized in the game.
 * Any previously loaded board is replaced.
 * @author ujsap
 */
public class LoadBoardCommand implements Command<Game> {
    private final List<String> inputLines;

    /**
     * Creates a new LoadBoardCommand and reads the given board file.
     * @param file the path to the board file
     * @throws InvalidArgumentException if the file cannot be read
     */
    public LoadBoardCommand(String file) throws InvalidArgumentException {
        this.inputLines = FilesReader.readInputFile(file.trim());
    }

    /**
     * Executes the command: loads the board into the game.
     * @param handle the game instance
     * @return a {@link Result} containing the verbatim board definition on success,
     *         or an error result if parsing fails
     */
    @Override
    public Result execute(Game handle) {
        try {
            handle.loadBoard(inputLines);
        } catch (BoardParserException e) {
            return Result.error(PrintHelpers.prepareVerbatimPrint(inputLines) + System.lineSeparator()
                    + e.getMessage());
        }
        return Result.success(PrintHelpers.prepareVerbatimPrint(inputLines));
    }
}
