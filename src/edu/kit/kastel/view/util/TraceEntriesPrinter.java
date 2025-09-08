package edu.kit.kastel.view.util;

import edu.kit.kastel.model.board.Position;
import edu.kit.kastel.model.tree.TraceEntry;
import edu.kit.kastel.model.tree.nodes.leafs.actions.ActionType;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.ConditionType;

/**
 * Utility class for formatting {@link TraceEntry} objects for output.
 * In particular, this class handles special formatting for nodes that
 * contain coordinates, by converting them into user-facing view coordinates.
 * @author ujsap
 */
public final class TraceEntriesPrinter {
    private static final String COORDINATES_SEPARATOR = " ";
    private static final String COORDINATES_SPLITTER = ",";

    private TraceEntriesPrinter() {
        // Utility class
    }

    /**
     * Formats a trace entry for output.
     * If the entry is a coordinate-related action or condition,coordinates are converted to view format.
     * Otherwise, the entry's default string representation is returned.
     * @param entry the trace entry to format
     * @return the formatted string
     */
    public static String format(TraceEntry entry) {
        String nodeType = entry.nodeType();

        if (nodeType.startsWith(ActionType.FLY.toString())
                || nodeType.startsWith(ConditionType.EXISTS_PATH_TO.toString())) {
            return formatWithCoordinates(entry);
        }
        return entry.toString();
    }

    private static String formatWithCoordinates(TraceEntry entry) {
        String[] parts = entry.nodeType().split(COORDINATES_SEPARATOR);
        String[] coordinates = parts[1].split(COORDINATES_SPLITTER);
        int col = Integer.parseInt(coordinates[0]);
        int row = Integer.parseInt(coordinates[1]);

        String viewCoordinates = PrintHelpers.toViewPosition(new Position(col, row));
        String newNodeType = parts[0] + COORDINATES_SEPARATOR + viewCoordinates.trim();

        return new TraceEntry(entry.ladybugId(), entry.nodeId(), newNodeType, entry.event()).toString();
    }
}
