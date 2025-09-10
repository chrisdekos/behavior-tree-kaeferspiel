package edu.kit.kastel.view.exceptions;

/**
 * Exception thrown when an operation is attempted before the board has been loaded.
 * @author ujsap
 */
public class BoardNotLoadedException extends Exception {
    private static final String LOAD_BOARD_FIRST_ERROR = "board must be loaded first";

    /**
     * Creates a new BoardNotLoadedException with the default error message.
     */
    public BoardNotLoadedException() {
        super(LOAD_BOARD_FIRST_ERROR);
    }
}
