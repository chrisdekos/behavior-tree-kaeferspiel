package edu.kit.kastel.model.tree.nodes.composites;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;

import java.util.ArrayList;
import java.util.Map;

/**
 * Abstract base class for all composite nodes in a behavior tree.
 * A composite node manages a list of children and controls their execution according to its specific logic.
 * @author ujsap
 */
public abstract class CompositeNode extends Node {
    private static final int NEXT_INDEX_NUMBER = 1;

    /**
     * Creates a new CompositeNode.
     * @param id       the node id
     * @param nodeType the type of this composite node
     * @param parent   the parent node
     */
    protected CompositeNode(String id, NodeType nodeType, Node parent) {
        super(id, nodeType, parent);
        this.children = new ArrayList<>();
    }

    /**
     * Executes this composite node for one tick.
     * @param tickContext the tick context
     * @return the resulting status
     */
    @Override
    public abstract NodeStatus tick(TickContext tickContext);

    /**
     * Ensures the entry of this node is logged if its status is still {@link NodeStatus#ENTRY}.
     * @param tickContext the tick context
     */
    protected final void ensureEntry(TickContext tickContext) {
        if ((getNodeStatus() == NodeStatus.ENTRY)) {
            logEntry(tickContext);
            this.setNodeStatus(NodeStatus.RUNNING);
        }
    }

    /**
     * Inserts a new child node directly after an existing child.
     * @param existing the existing child node
     * @param newChild the new child node to insert
     */
    @Override
    public void insertChildAfter(Node existing, Node newChild) {
        int existingIndex = indexOfChild(existing);
        children.add(existingIndex + NEXT_INDEX_NUMBER, newChild);
        newChild.setParent(this);
    }

    /**
     * Registers this node and all of its children in the given index.
     * @param index a map from node ids to node instances
     */
    @Override
    public void registerSubtree(Map<String, Node> index) {
        index.put(getId(), this);
        for (Node child : children) {
            child.registerSubtree(index);
        }
    }

    /**
     * Executes children sequentially until one matches the breakOn status,or all finish without it,
     * in which case defaultIfNoBreak is returned.
     * If an action node is encountered, it becomes the current node,
     * execution stops, and {@link NodeStatus#RUNNING} is returned.
     *
     * @param tickContext       the tick context
     * @param breakOn           the status that causes early termination
     * @param defaultIfNoBreak  the status if no child produced breakOn
     * @return the resulting node status
     */
    protected final NodeStatus runChildrenLinear(TickContext tickContext,
                                                 NodeStatus breakOn,
                                                 NodeStatus defaultIfNoBreak) {

        ensureEntry(tickContext);
        for (Node child : children) {
            NodeStatus preStatus = child.getNodeStatus();
            if (preStatus == breakOn) {
                setNodeStatus(preStatus);
                logExit(tickContext, preStatus);
                return preStatus;
            }

            if (child.getNodeStatus().isFinished()) {
                continue;
            }

            if (child.getNodeType().isAction()) {
                tickContext.getLadybug().getBehaviorTree().setCurrentNode(child);
                tickContext.requestStop();
                return NodeStatus.RUNNING;
            }

            child.tick(tickContext);
            if (tickContext.isStopRequested()) {
                return NodeStatus.RUNNING;
            }

            NodeStatus postStatus = child.getNodeStatus();
            if (postStatus == breakOn) {
                setNodeStatus(postStatus);
                logExit(tickContext, postStatus);
                return postStatus;
            }
        }
        setNodeStatus(defaultIfNoBreak);
        logExit(tickContext, defaultIfNoBreak);
        return getNodeStatus();
    }

    /**
     * Resets this composite node and all its children to {@link NodeStatus#ENTRY}.
     */
    @Override
    public void resetState() {
        setNodeStatus(NodeStatus.ENTRY);
        for (Node child : children) {
            child.setNodeStatus(NodeStatus.ENTRY);
        }
    }

    /**
     * Gets the specific type of this node, which is the string form of its {@link NodeType}.
     * @return the node type string
     */
    @Override
    public String getSpecificType() {
        return getNodeType().toString();
    }
}
