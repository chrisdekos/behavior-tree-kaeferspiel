package edu.kit.kastel.model.exceptions;

/**
 * Thrown when a board description cannot be parsed correctly.
 * @author ujsap
 */
public class BoardParserException extends Exception {
    /**
     * Creates a new exception with the given message.
     * @param message a description of the parsing error
     */
    public BoardParserException(String message) {
        super(message);
    }
}