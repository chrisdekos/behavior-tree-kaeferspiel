package edu.kit.kastel.model.exceptions;

/**
 * Thrown when a behavior tree description cannot be parsed correctly.
 * @author ujsap
 */
public class TreeParserException extends Exception {
    /**
     * Creates a new exception with the given message.
     * @param message a description of the parsing error
     */
    public TreeParserException(String message) {
        super(message);
    }
}
