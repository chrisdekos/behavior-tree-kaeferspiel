/*
 * Copyright (c) 2025, KASTEL. All rights reserved.
 */

package edu.kit.kastel.view;

import edu.kit.kastel.view.exceptions.InvalidArgumentException;

/**
 * This interface provides a command instance constructed with the given arguments.
 * @param <T> the type of the value that is handled by the command
 * @author ujsap
 */
public interface CommandProvider<T> {

    /**
     * Constructs a new command instance with the given arguments.
     * @param arguments the arguments to be used for constructing the command
     * @return the constructed command
     * @throws InvalidArgumentException if parsing/retrieving an argument fails
     */
    Command<T> provide(Arguments arguments) throws InvalidArgumentException;
}
