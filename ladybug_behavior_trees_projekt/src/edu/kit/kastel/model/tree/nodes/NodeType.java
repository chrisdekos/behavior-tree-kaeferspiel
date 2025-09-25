package edu.kit.kastel.model.tree.nodes;

/**
 * Represents the different types of nodes in a behavior tree.
 * @author ujsap
 */
public enum NodeType {
    /**
     * A fallback node that executes its children in order until one succeeds.
     */
    FALLBACK,

    /**
     * A sequence node that executes its children in order until one fails.
     */
    SEQUENCE,

    /**
     * A parallel node that executes all his children in order and checks, how many of them succeeded.
     * It compares this number with a threshold
     */
    PARALLEL,

    /**
     * An action node that performs a specific action.
     */
    ACTION,

    /**
     * A condition node that checks a specific condition .
     */
    CONDITION;

    /**
     * Checks whether this node type is a composite.
     * @return true if composite, false otherwise
     */
    public boolean isComposite() {
        return this == FALLBACK || this == SEQUENCE || this == PARALLEL;
    }

    /**
     * Checks whether this node type is an action.
     * @return true if it is an action, false otherwise
     */
    public boolean isAction() {
        return this == ACTION;
    }

    /**
     * Returns the lowercase string representation of this type.
     * @return the type name in lowercase
     */
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
