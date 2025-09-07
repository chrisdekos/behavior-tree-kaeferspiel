package edu.kit.kastel.model.tree;

import edu.kit.kastel.model.board.Board;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.board.Position;

import java.util.Collections;
import java.util.Set;

/**
 * Contains all relevant information for a single tick of the behavior tree.
 * Provides access to the board, the controlled {@link Ladybug}, the positions of all ladybugs
 * and the {@link Trace} for logging events. It also allows requesting an early stop of the current tick.
 * @author ujsap
 */
public class TickContext {

    private final Board board;
    private final Ladybug ladybug;
    private final Trace trace;
    private final Set<Position> ladybugPositions;
    private boolean stopRequested;

    /**
     * Creates a new tick context.
     * @param board            the board state
     * @param ladybug          the ladybug controlled during this tick
     * @param trace            the trace that records execution events
     * @param ladybugPositions the positions of all ladybugs on the board
     */
    public TickContext(Board board, Ladybug ladybug, Trace trace, Set<Position> ladybugPositions) {
        this.board = board;
        this.trace = trace;
        this.ladybug = ladybug;
        this.ladybugPositions = ladybugPositions;
        this.stopRequested = false;
    }

    /**
     * Requests that the current tick stops execution prematurely.
     */
    public void requestStop() {
        this.stopRequested = true;
    }

    /**
     * Checks if a stop has been requested for the current tick.
     * @return true if stop was requested, false otherwise
     */
    public boolean isStopRequested() {
        return stopRequested;
    }

    /**
     * Gets the board of the tick context.
     * @return the board state
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the ladybug of the tick context.
     * @return the ladybug controlled during this tick
     */
    public Ladybug getLadybug() {
        return ladybug;
    }

    /**
     * Gets the ladybug positions of the tick context.
     * @return an unmodifiable view of the positions of all ladybugs
     */
    public Set<Position> getLadybugPositions() {
        return Collections.unmodifiableSet(ladybugPositions);
    }

    /**
     * Gets the trace of the tick context.
     * @return the trace that logs execution events for this tick
     */
    public Trace getTrace() {
        return trace;
    }
}
