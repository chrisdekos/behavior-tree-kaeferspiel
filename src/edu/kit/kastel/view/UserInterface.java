/*
 * Copyright (c) 2025, KASTEL. All rights reserved.
 */

package edu.kit.kastel.view;

import edu.kit.kastel.model.Game;
import edu.kit.kastel.view.commands.ModelKeyword;
import edu.kit.kastel.view.commands.ViewKeyword;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p> The class initiates and handles an interaction with the user. It is responsible for delegating the input to the
 * corresponding command implementations. The command and its arguments are expected to be separated by {@value #COMMAND_SEPARATOR}.</p>
 *
 * <p> An interaction is started by calling {@link #handleUserInput()}. It is possible to stop the current interaction prematurely
 * with {@link #stop()}.</p>
 *
 * <p> As example, if the interaction should happen via the command line, the standard input/output streams should be provided.</p>
 *
 * @author Programmieren-Team
 * @author ujsap
 */
public class UserInterface {

    private static final String COMMAND_SEPARATOR = " ";
    private static final String ERROR_PREFIX = "Error, ";
    private static final String ERROR_UNKNOWN_COMMAND_FORMAT = ERROR_PREFIX + " unknown command: ";
    private static final String ERROR_TOO_MANY_ARGUMENTS = ERROR_PREFIX + "too many arguments provided.";
    private static final String ERROR_INVALID_PRECONDITION = ERROR_PREFIX + "command cannot be used right now.";
    private final Set<ModelKeyword> gameKeywords = EnumSet.allOf(ModelKeyword.class);
    private final Set<ViewKeyword> viewKeywords = EnumSet.allOf(ViewKeyword.class);
    private final InputStream inputSource;
    private final PrintStream defaultStream;
    private final PrintStream errorStream;
    private boolean isRunning;
    private final Game game;

    /**
     * Constructs a new user interface using the provided input source and output streams when interacting.
     * The provided input source is closed after the interaction is finished.
     *
     * @param inputSource the input source used to retrieve the user input
     * @param defaultOutputStream the stream used to print the default output
     * @param errorStream the stream used to print the error output
     */
    public UserInterface(InputStream inputSource, PrintStream defaultOutputStream, PrintStream errorStream) {
        this.inputSource = inputSource;
        this.defaultStream = defaultOutputStream;
        this.errorStream = errorStream;
        this.game = createGame();
    }

    /**
     * Creates the game instance that is provided by this user interface to its managed commands.
     */
    private Game createGame() {
        return new Game();
    }

    /**
     * Stops this instance from reading further input from the source.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Starts the interaction with the user. This method will block while interacting.
     * The interaction will continue as long as the provided source has more lines to read,
     * or until it is stopped. The provided input source is closed after the interaction is finished.
     *
     * @see Scanner#hasNextLine()
     * @see #stop()
     * @see #UserInterface(InputStream, PrintStream, PrintStream)
     */
    public void handleUserInput() {
        this.isRunning = true;
        try (Scanner scanner = new Scanner(this.inputSource)) {
            while (this.isRunning && scanner.hasNextLine()) {
                handleLine(scanner.nextLine());
            }
        }
    }

    private void handleLine(String line)  {
        String[] splitLine = line.split(COMMAND_SEPARATOR, -1);
        if (!findAndHandleCommand(this.viewKeywords, this, splitLine)
                && !findAndHandleCommand(this.gameKeywords, this.game, splitLine)) {
            this.errorStream.println(ERROR_UNKNOWN_COMMAND_FORMAT + line);
        }
    }

    private <S, T extends Keyword<S>> boolean findAndHandleCommand(Set<T> keywords, S value, String[] command) {
        T keyword = retrieveKeyword(keywords, command);
        if (keyword != null) {
            String[] arguments = Arrays.copyOfRange(command, keyword.words(), command.length);
            handleCommand(value, arguments, keyword);
            return true;
        }
        return false;
    }

    private <S, T extends Keyword<S>> void handleCommand(S value, String[] arguments, T keyword) {
        if (value == null) {
            this.errorStream.println(ERROR_INVALID_PRECONDITION);
            return;
        }
        Arguments argumentsHolder = new Arguments(arguments);
        Command<S> providedCommand;
        try {
            providedCommand = keyword.provide(argumentsHolder);
        } catch (InvalidArgumentException e) {
            this.errorStream.println(ERROR_PREFIX + e.getMessage());
            return;
        }
        if (!argumentsHolder.isExhausted()) {
            this.errorStream.println(ERROR_TOO_MANY_ARGUMENTS);
            return;
        }
        handleResult(providedCommand.execute(value));
    }

    private void handleResult(Result result) {
        if (result == null) {
            return;
        }
        if (result.getMessage() == null) {
            return;
        }
        PrintStream outputStream = switch (result.getType()) {
            case SUCCESS -> this.defaultStream;
            case FAILURE -> this.errorStream;
        };
        outputStream.println((result.getType().equals(ResultType.FAILURE) ? ERROR_PREFIX : "") + result.getMessage());
    }

    private static <T extends Keyword<?>> T retrieveKeyword(Collection<T> keywords, String[] command) {
        for (T keyword : keywords) {
            if (keyword.matches(command)) {
                return keyword;
            }
        }
        return null;
    }
}
