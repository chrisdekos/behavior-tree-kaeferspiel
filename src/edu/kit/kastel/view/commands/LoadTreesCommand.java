package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.exceptions.TreeParserException;
import edu.kit.kastel.model.tree.BehaviorTree;
import edu.kit.kastel.view.BoardNotLoadedException;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.InvalidArgumentException;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.util.FilesReader;
import edu.kit.kastel.view.util.PrintHelpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to load behavior trees into the game.
 * Each provided file is printed verbatim and then parsed.
 * If any error occurs, the game state remains unchanged and the error is returned.
 * @author ujsap
 */
public class LoadTreesCommand implements Command<Game> {
    private final List<String> files;

    /**
     * Creates a new LoadTreesCommand.
     * @param files the list of file paths that contain behavior tree definitions
     */
    public LoadTreesCommand(List<String> files)  {
        this.files = files;
    }

    /**
     * Loads trees from the given files and assigns them to free ladybugs.
     * Requires the board to be loaded first, otherwise fails.
     * Prints each file's contents verbatim.
     * Parses trees file-by-file; if parsing any file fails, nothing is committed.
     * On success, assigns all parsed trees to the next free ladybugs and marks them active.
     * @param handle the game instance
     * @return {@link Result#success()} on success; otherwise an error result containing the message
     */
    @Override
    public Result execute(Game handle) {
        try {
            if (!handle.isBoardLoaded()) {
                throw new BoardNotLoadedException();
            }
            List<BehaviorTree> allTrees = new ArrayList<>();
            int assigned = 0;

            for (String file : files) {
                List<String> lines = FilesReader.readInputFile(file);

                System.out.print(PrintHelpers.prepareVerbatimPrint(lines));

                List<Ladybug> freeLadybugs = handle.getInitialLadybugs()
                        .subList(assigned, handle.getInitialLadybugs().size());
                List<BehaviorTree> trees = handle.loadTreeFile(lines, freeLadybugs);

                allTrees.addAll(trees);
                assigned += trees.size();
            }
            handle.commitTrees(allTrees);
            return Result.success();

        } catch (TreeParserException | InvalidArgumentException | BoardNotLoadedException e) {
            return Result.error(e.getMessage());
        }
    }
}
