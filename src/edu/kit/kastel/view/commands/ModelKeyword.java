package edu.kit.kastel.view.commands;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.view.CommandProvider;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.Keyword;
import edu.kit.kastel.view.Arguments;
import edu.kit.kastel.view.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;

/**
 * Keywords for all model-related commands in the {@link Game}.
 * Each keyword maps user input to a corresponding {@link Command}.
 * @author ujsap
 */
public enum ModelKeyword implements Keyword<Game> {
    /**
     * The keyword for the {@link AddSiblingCommand place} command.
     */
    ADD_SIBLING(arguments -> new AddSiblingCommand(
            arguments.parsePositive(), arguments.parseString(), arguments.parseString())),
    /**
     * The keyword for the {@link HeadCommand move} command.
     */
    HEAD(arguments -> new HeadCommand(arguments.parsePositive())),
    /**
     * The keyword for the {@link JumpToCommand pass} command.
     */
    JUMP_TO(arguments -> new JumpToCommand(arguments.parsePositive(), arguments.parseString())),
    /**
     * The keyword for the {@link ListLadybugsCommand pass} command.
     */
    LIST_LADYBUGS(arguments -> new ListLadybugsCommand()),
    /**
     * The keyword for the {@link LoadBoardCommand pass} command.
     */
    LOAD_BOARD(arguments -> new LoadBoardCommand(arguments.parseString())),
    /**
     * The keyword for the {@link LoadTreesCommand pass} command.
     */
    LOAD_TREES(arguments -> {
        List<String> paths = new ArrayList<>();
        while (!arguments.isExhausted()) {
            paths.add(arguments.parseString());
        } return new LoadTreesCommand(paths); }),
    /**
     * The keyword for the {@link NextActionCommand pass} command.
     */
    NEXT_ACTION(arguments -> new NextActionCommand()),
    /**
     * The keyword for the {@link PrintPositionCommand pass} command.
     */
    PRINT_POSITION(arguments -> new PrintPositionCommand(arguments.parsePositive())),
    /**
     * The keyword for the {@link ResetTreeCommand pass} command.
     */
    RESET_TREE(arguments -> new ResetTreeCommand(arguments.parsePositive()));

    private static final String VALUE_NAME_DELIMITER = "_";
    private final CommandProvider<Game> provider;

    ModelKeyword(CommandProvider<Game> provider) {
        this.provider = provider;
    }

    @Override
    public Command<Game> provide(Arguments arguments) throws InvalidArgumentException {
        return provider.provide(arguments);
    }

    @Override
    public boolean matches(String[] command) {
        String[] splittedKeyword = name().split(VALUE_NAME_DELIMITER);
        if (command.length < splittedKeyword.length) {
            return false;
        }
        for (int i = 0; i < splittedKeyword.length; i++) {
            if (!splittedKeyword[i].toLowerCase().equals(command[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int words() {
        return name().split(VALUE_NAME_DELIMITER).length;
    }
}