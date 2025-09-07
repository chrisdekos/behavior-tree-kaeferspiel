package edu.kit.kastel.model.tree;

/**
 * Represents a single entry in a {@link Trace}.
 * Each entry records which ladybug executed which node, of which type, and what event occurred.
 * @param ladybugId the id of the ladybug
 * @param nodeId    the id of the node
 * @param nodeType  the specific type of the node
 * @param event     the event that occurred for the node
 * @author ujsap
 */
public record TraceEntry(
        int ladybugId,
        String nodeId,
        String nodeType,
        TraceEvent event
) {

    /**
     * Separator used in {@link #toString()}.
     */
    public static final String WHITESPACE = " ";

    /**
     * Returns a string representation of this trace entry in the form: ladybugId nodeId nodeType event.
     * @return a string representation of this entry
     */
    @Override
    public String toString() {
        return ladybugId + WHITESPACE + nodeId + WHITESPACE + nodeType + WHITESPACE + event;
    }
}
