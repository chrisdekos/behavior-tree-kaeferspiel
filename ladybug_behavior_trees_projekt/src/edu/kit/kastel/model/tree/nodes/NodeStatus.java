package edu.kit.kastel.model.tree.nodes;

/**
 * Represents the execution status of a node in a behavior tree.
 * @author ujsap
 */
public enum NodeStatus {
    /**
     * The node finished execution successfully.
     */
    SUCCESS,

    /**
     * The node finished execution but failed.
     */
    FAILURE,

    /**
     * The node is started but did not finish yet.
     */
    RUNNING,

    /**
     *  The node has not been executed yet.
     */
    ENTRY;

    /**
     * Checks whether this status represents a finished state.
     * @return true if the status is {@link #SUCCESS} or {@link #FAILURE}, false otherwise
     */
    public boolean isFinished() {
        return this == SUCCESS || this == FAILURE;
    }
}
