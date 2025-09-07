package edu.kit.kastel.model.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the execution trace of a behavior tree tick.
 * A trace is an ordered list of {@link TraceEntry} objects, each recording an event for a node.
 * @author ujsap
 */
public class Trace {
    private final List<TraceEntry> entries = new ArrayList<>();

    /**
     * Adds a new entry to this trace.
     * @param entry the entry to add
     */
    public void addEntry(TraceEntry entry) {
        entries.add(entry);
    }

    /**
     * Returns all trace entries in order.
     * @return an unmodifiable list of trace entries
     */
    public List<TraceEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

}
