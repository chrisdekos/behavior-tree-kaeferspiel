package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.exceptions.TreeParserException;
import edu.kit.kastel.model.tree.BehaviorTree;
import edu.kit.kastel.view.exceptions.BoardNotLoadedException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.exceptions.InvalidArgumentException;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.util.FilesReader;
import edu.kit.kastel.view.util.PrintHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.kit.kastel.view.Arguments.ERROR_TOO_FEW_ARGUMENTS;

/**
 * Command to load behavior trees into the game.
 * Each provided file is printed verbatim and then parsed.
 * If any error occurs, the game state remains unchanged and the error is returned.
 * @author Programmieren-Team
 * @author ujsap
 */
public class LoadTreesCommand implements Command<Game> {
    private static final String MORE_TREES_THAN_LADYBUGS_ERROR = "there can not be more trees than ladybugs";
    private static final int INITIAL_NUMBER_OF_ASSIGNED_LADYBUGS = 0;
    private final List<String> files;

    /**
     * Creates a new LoadTreesCommand.
     * @param files the list of file paths that contain behavior tree definitions
     */
    public LoadTreesCommand(List<String> files) {

        this.files = Collections.unmodifiableList(files);
    }

    /**
     * Loads trees from the given files and assigns them to free ladybugs.
     * Requires the board to be loaded first, otherwise fails.
     * Prints each file's contents verbatim.
     * Parses trees file-by-file, while parsing, if any file fails, nothing is committed.
     * On success, assigns all parsed trees to the next free ladybugs and marks them active.
     * @param handle the game instance
     * @return {@link Result#success()} on success; otherwise an error result containing the message
     */
    @Override
    public Result execute(Game handle) {
        List<BehaviorTree> allTrees = new ArrayList<>();
        int assigned = INITIAL_NUMBER_OF_ASSIGNED_LADYBUGS;

        if (files.size() > handle.getInitialLadybugs().size()) {
            return Result.error(new InvalidArgumentException(MORE_TREES_THAN_LADYBUGS_ERROR).getMessage());
        }
        if (!handle.isBoardLoaded()) {
            return Result.error(new BoardNotLoadedException().getMessage());
        }
        if (files.isEmpty()) {
            return Result.error(new InvalidArgumentException(ERROR_TOO_FEW_ARGUMENTS).getMessage());
        }
        try {
            for (String file : files) {
                List<String> lines = FilesReader.readInputFile(file);
                System.out.println(PrintHelpers.prepareVerbatimPrint(lines));

                List<Ladybug> freeLadybugs = handle.getInitialLadybugs()
                        .subList(assigned, handle.getInitialLadybugs().size());
                List<BehaviorTree> trees = handle.loadTreeFile(lines, freeLadybugs);

                allTrees.addAll(trees);
                assigned += trees.size();
            }
        } catch (TreeParserException | InvalidArgumentException e) {
            return Result.error(e.getMessage());
        }
        handle.commitTrees(allTrees);
        return Result.success();
    }
}
