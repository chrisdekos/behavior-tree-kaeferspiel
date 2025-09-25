package edu.kit.kastel.view.exceptions;

/**
 * Exception thrown when a command is requested before both the board and the behavior trees are loaded.
 * @author ujsap
 */
public class AllActionsEnabledException extends Exception {
    private static final String BOARD_AND_TREES_NOT_LOADED_ERROR = "board and trees must be loaded first";

    /**
     * Creates a new exception with the default error message.
     */
    public AllActionsEnabledException() {
        super(BOARD_AND_TREES_NOT_LOADED_ERROR);
    }
}
