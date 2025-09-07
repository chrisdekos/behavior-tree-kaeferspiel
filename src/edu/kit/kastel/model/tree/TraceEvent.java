package edu.kit.kastel.model.tree;

/**
 * Represents the type of event recorded in a {@link TraceEntry}.
 * @author ujsap
 */
public enum TraceEvent {
    /**
     * Shows that a node has been entered.
     */
    ENTRY,

    /**
     * Shows that a node finished execution successfully.
     */
    SUCCESS,

    /**
     * Shows that a node finished execution with failure.
     */
    FAILURE
}
