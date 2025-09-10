/*
 * Copyright (c) 2025, KASTEL. All rights reserved.
 */

package edu.kit.kastel.view;

/**
 * This interface represents a command that can be executed to handle the given value.
 * @param <T> the type of the value to be handled
 * @author Programmieren-Team
 */
@FunctionalInterface
public interface Command<T> {
    /**
     * A constant with an error message, in case a ladybug could not be found.
     * It is used by many command classes.
     */
    String COULD_NOT_FIND_LADYBUG_ERROR = "ladybug could not be found";

    /**
     * A constant with an error message, in case a node could not be found.
     * It was used by many command classes.
     */
    String COULD_NOT_FIND_NODE_ERROR = "node could not be found";


    /**
     * Executes the command to handle the given value.
     * @param handle the value to be handled
     * @return the result of the command execution
     */
    Result execute(T handle);
}