package edu.kit.kastel.view.commands;

import edu.kit.kastel.view.Command;
import edu.kit.kastel.view.Result;
import edu.kit.kastel.view.UserInterface;

/**
 * Command to quit the application by stopping the {@link UserInterface}.
 * @author ujsap
 */
public class QuitCommand implements Command<UserInterface> {

    /**
     * Executes the quit command: stops the user interface.
     * @param handle the user interface to stop
     * @return a {@link Result} indicating success
     */
    @Override
    public Result execute(UserInterface handle) {
        handle.stop();
        return Result.success();
    }
}
