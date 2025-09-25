package edu.kit.kastel.model.tree.nodes;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.TraceEntry;
import edu.kit.kastel.model.tree.TraceEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for all nodes in a behavior tree.
 * A node has: an id for identification, a node type, a parent, a list of children and a status to track its state.
 * Subclasses implement their own execution logic in {@link #tick(TickContext)}.
 * @author ujsap
 */
public abstract class Node {
    private static final int NEXT_INDEX_NUMBER = 1;

    private final List<Node> children;
    private final String id;
    private final NodeType nodeType;
    private Node parent;
    private NodeStatus nodeStatus;

    /**
     * Creates a new node.
     * @param id       the node id
     * @param nodeType the type of this node
     * @param parent   the parent node
     */
    protected Node(String id, NodeType nodeType, Node parent) {
        this.id = id;
        this.nodeType = nodeType;
        this.parent = parent;
        children = new ArrayList<>();
        this.nodeStatus = NodeStatus.ENTRY;
    }

    /**
     * Executes this node for one tick.
     * @param tickContext the context for this tick
     * @return the resulting status
     */
    public abstract NodeStatus tick(TickContext tickContext);

    /**
     * Sets the parent node to a given node.
     * @param parent the given node
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Inserts a new child node directly after an existing child.
     * @param existing the existing child node
     * @param newChild the new child node to insert
     */
    public void insertChildAfter(Node existing, Node newChild) {
        int existingIndex = indexOfChild(existing);
        children.add(existingIndex + NEXT_INDEX_NUMBER, newChild);
        newChild.setParent(this);
    }

    /**
     * Gets the node id.
     * @return the node id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the node type.
     * @return the type of this node
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * Logs an entry event for this node and sets its status to {@link NodeStatus#RUNNING}.
     * @param tickContext the current tick context containing board, ladybug, and trace
     */
    protected final void logEntry(TickContext tickContext) {
        setNodeStatus(NodeStatus.RUNNING);
        tickContext.getTrace().addEntry(
                new TraceEntry(tickContext.getLadybug().getId(), id, getSpecificType(), TraceEvent.ENTRY)
        );
    }

    /**
     * Logs an exit event for this node and sets its status to the given result.
     * @param tickContext the tick context
     * @param status      the final status
     */
    protected final void logExit(TickContext tickContext, NodeStatus status) {
        setNodeStatus(status);
        tickContext.getTrace().addEntry(
                new TraceEntry(
                        tickContext.getLadybug().getId(),
                        id,
                        getSpecificType(),
                        status == NodeStatus.SUCCESS ? TraceEvent.SUCCESS : TraceEvent.FAILURE
                )
        );
    }

    /**
     * Gets the specific type of node, in string representation.
     * @return a more specific type
     */
    public abstract String getSpecificType();

    /**
     * Registers this node and its subtree in the given index.
     * @param index a map from node ids to node instances
     */
    public abstract void registerSubtree(Map<String, Node> index);

    /**
     * Adds a child to this node and updates its parent reference.
     * @param child the child node to add
     */
    public void addChild(Node child) {
        this.children.add(child);
        child.setParent(this);
    }

    /**
     * Gets the index of a given child.
     * @param child the child node to find the index from
     * @return the index of the given child
     */
    private int indexOfChild(Node child) {
        return this.children.indexOf(child);
    }

    /**
     * Gets a list of the children.
     * @return the list of children
     */
    public final List<Node> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    /**
     * Resets the internal state of this node.
     * Is an empty method, so subclasses from node can override it.
     */
    public void resetState() {
    }

    /**
     * Gets the parent of a node.
     * @return the parent node
     */
    public Node getParent() {
        return this.parent;
    }

    /**
     * Updates the current node status.
     * @param nodeStatus the status to be set
     */
    public void setNodeStatus(NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    /**
     * Gets the node status of a node.
     * @return the current node status
     */
    public NodeStatus getNodeStatus() {
        return this.nodeStatus;
    }
}
