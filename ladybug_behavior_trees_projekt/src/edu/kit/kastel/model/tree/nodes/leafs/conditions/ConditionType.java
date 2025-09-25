package edu.kit.kastel.model.tree.nodes.leafs.conditions;

import edu.kit.kastel.model.exceptions.TreeParserException;

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

    private static final String EXISTS_PATH_TOO_MANY_COORDINATES = "existsPath can have 1 or 2 coordinates.";
    private static final int INITIAL_COUNT = 0;
    private static final int ONE_COORDINATE_FOUND = 1;
    private static final int TWO_COORDINATES_FOUND = 2;
    private static final String REGEX_GROUP_COORDINATES = "coordinates";

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
     * @throws TreeParserException if representation has more than 2 coordinates.
     */
    public static ConditionType fromRepresentation(String representation) throws TreeParserException {
        Matcher existsPathMatcher = EXISTS_PATH_REGEX.matcher(representation);
        if (existsPathMatcher.matches()) {
            String coordinates = existsPathMatcher.group(REGEX_GROUP_COORDINATES);
            Matcher coordinatesMatcher = COORDINATES_REGEX.matcher(coordinates);

            int count = INITIAL_COUNT;
            while (coordinatesMatcher.find()) {
                count++;
            }
            if (count == ONE_COORDINATE_FOUND) {
                return EXISTS_PATH_TO;
            } else if (count == TWO_COORDINATES_FOUND) {
                return EXISTS_PATH_BETWEEN;
            } else {
                throw new TreeParserException(EXISTS_PATH_TOO_MANY_COORDINATES);
            }
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
