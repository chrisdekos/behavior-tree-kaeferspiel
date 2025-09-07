package edu.kit.kastel;

import edu.kit.kastel.view.UserInterface;

/**
 * Entry point for the KaraTrees application.
 * KaraTrees initializes the {@link UserInterface} and starts
 * the main loop to handle user input from the command line.
 * @author Programmieren-Team
 * @author ujsap
 */
public final class KaraTrees {

    private KaraTrees() {
        // Utility class: prevent instantiation
    }

    /**
     * Starts the KaraTrees application.
     * @param args command-line arguments, which are never used
     */
    public static void main(String[] args) {
        UserInterface userInterface = new UserInterface(System.in, System.out, System.err);
        userInterface.handleUserInput();
    }
}
