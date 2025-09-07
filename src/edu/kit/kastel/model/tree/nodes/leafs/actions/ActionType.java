package edu.kit.kastel.model.tree.nodes.leafs.actions;

/**
 * Represents the different action types that a behavior tree node can perform.
 * Each action type has a string representation used when parsing trees.
 * @author ujsap
 */
public enum ActionType {
    /** Move one step forward. */
    MOVE("move"),

    /** Turn 90 degrees to the left. */
    TURN_LEFT("turnLeft"),

    /** Turn 90 degrees to the right. */
    TURN_RIGHT("turnRight"),

    /** Pick up a leaf from the current cell. */
    TAKE_LEAF("takeLeaf"),

    /** Place a leaf on the current cell. */
    PLACE_LEAF("placeLeaf"),

    /** Fly directly to a target position. */
    FLY("fly");

    private final String representation;

    ActionType(String representation) {
        this.representation = representation;
    }

    /**
     * Converts a string identifier to an action type.
     * @param identifier the string form of an action
     * @return the matching action type, or null if none matches
     */
    public static ActionType fromString(String identifier) {
        for (ActionType type : values()) {
            if (type.representation.equals(identifier)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Returns the string representation of a specific type.
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.representation;
    }
}
