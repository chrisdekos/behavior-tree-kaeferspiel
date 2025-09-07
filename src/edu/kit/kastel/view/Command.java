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
     * Executes the command to handle the given value.
     * @param handle the value to be handled
     * @return the result of the command execution
     */
    Result execute(T handle);
}