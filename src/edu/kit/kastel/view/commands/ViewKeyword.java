package edu.kit.kastel.view.commands;

import edu.kit.kastel.view.UserInterface;
import edu.kit.kastel.view.CommandProvider;
import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.Keyword;
import edu.kit.kastel.view.Arguments;
import edu.kit.kastel.view.exceptions.InvalidArgumentException;


/**
 * This enum represents all keywords for commands handling an {@link UserInterface}.
 * @author Programmieren-Team
 * @author ujsap
 */
public enum ViewKeyword implements Keyword<UserInterface> {

    /**
     * The keyword for the {@link QuitCommand quit} command.
     */
    QUIT(arguments -> new QuitCommand());

    private static final String VALUE_NAME_DELIMITER = "_";
    private static final int FIRST_PART = 0;
    private final CommandProvider<UserInterface> provider;

    ViewKeyword(CommandProvider<UserInterface> provider) {
        this.provider = provider;
    }

    @Override
    public Command<UserInterface> provide(Arguments arguments) throws InvalidArgumentException {
        return provider.provide(arguments);
    }

    @Override
    public boolean matches(String[] command)  {
        return name().toLowerCase().equals(command[FIRST_PART]);
    }

    @Override
    public int words() {
        return name().split(VALUE_NAME_DELIMITER).length;
    }
}