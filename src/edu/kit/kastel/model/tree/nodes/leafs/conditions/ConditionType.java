package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the different condition types that can be checked in a behavior tree.
 * Conditions describe the state of the environment.
 * @author ujsap
 */
public enum ConditionType {
    /**
     * Checks if the ladybug is at the edge of the board.
     */
    AT_EDGE("atEdge"),

    /**
     * Checks if there is a leaf in front of the ladybug.
     */
    LEAF_FRONT("leafFront"),

    /**
     * Checks if there is a tree in front of the ladybug.
     */
    TREE_FRONT("treeFront"),

    /**
     * Checks if there is a mushroom in front of the ladybug.
     */
    MUSHROOM_FRONT("mushroomFront"),

    /**
     * Checks if a path exists from the ladybug to a given position.
     */
    EXISTS_PATH_TO("existsPath"),

    /**
     * Checks if a path exists between two given positions.
     */
    EXISTS_PATH_BETWEEN("existsPath");

    // matches "existsPath" followed by coordinates
    private static final Pattern EXISTS_PATH_REGEX =
            Pattern.compile("^existsPath(?:\\s+(?<coordinates>.+))?$");

    // matches a coordinate pair "
    private static final Pattern COORDINATES_REGEX =
            Pattern.compile("-?\\d+\\s*,\\s*-?\\d+");

    private final String representation;

    ConditionType(String representation) {
        this.representation = representation;
    }

    /**
     * Identifies a condition type from its string representation.
     * For existsPath, the number of coordinates decides whether
     * the condition is {@link #EXISTS_PATH_TO} or {@link #EXISTS_PATH_BETWEEN}.
     * @param representation the full condition text
     * @return the matching condition type, or null if none matches
     */
    public static ConditionType fromRepresentation(String representation) {
        Matcher existsPathMatcher = EXISTS_PATH_REGEX.matcher(representation);
        if (existsPathMatcher.matches()) {
            String coordinates = existsPathMatcher.group("coordinates");
            Matcher coordinatesMatcher = COORDINATES_REGEX.matcher(coordinates);

            int count = 0;
            while (coordinatesMatcher.find()) {
                count++;
            }
            return (count >= 2) ? EXISTS_PATH_BETWEEN : EXISTS_PATH_TO;
        }
        for (ConditionType type : values()) {
            if (type.representation.equals(representation)) {
                return type;
            }
        }
        return null;
    }

    /**
     * The string representation of this condition type.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.representation;
    }
}
