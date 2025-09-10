package edu.kit.kastel.model.tree;

import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.board.Board;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.board.Position;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a behavior tree that controls the  movement of a {@link Ladybug}.
 * The tree has a root node, keeps saves of all nodes by their id,
 * and provides methods to tick the tree, reset it, and manipulate its execution flow.
 * @author ujsap
 */
public class BehaviorTree {
    private static final int INDEX_ADJUSTER = 1;
    private final Node root;
    private final Map<String, Node> nodesByID;
    private Node currentNode;
    private boolean jumpedSinceLastTick = false;

    /**
     * Creates a new behavior tree with the given root node.
     * @param root the root node of the tree
     */
    public BehaviorTree(Node root) {
        this.root = root;
        this.nodesByID = new HashMap<>();
        buildTreeByIndex(root);
        currentNode = root;
    }

    /**
     * Builds the internal node index starting from the root.
     * @param root the root node
     */
    private void buildTreeByIndex(Node root) {
        nodesByID.clear();
        root.registerSubtree(nodesByID);
    }

    /**
     * Executes one tick of the behavior tree for the given ladybug.
     * @param ladybug          the ladybug controlled by this tree
     * @param board            the board state
     * @param ladybugPositions the positions of all ladybugs
     * @return a trace of the executed actions and conditions
     */
    public Trace tick(Ladybug ladybug, Board board, Set<Position> ladybugPositions) {
        Trace trace = new Trace();
        TickContext tickContext = new TickContext(board, ladybug, trace, ladybugPositions);
        root.tick(tickContext);
        if (!tickContext.isStopRequested()) {
            resetTree();
            root.tick(tickContext);
        }
        currentNode.tick(tickContext);
        jumpedSinceLastTick = false;
        return trace;
    }

    /**
     * Gets the next node which is to be executed.
     * In case last executed node was an action and the last child of a composite, then returns the last executed.
     * @return the current head node of the tree
     */
    public Node head() {
        return peekNext();
    }

    private Node peekNext() {
        if (!currentNode.getNodeType().isAction()) {
            return root;
        }
        List<Node> children = currentNode.getParent().getChildren();
        int childIndex = children.indexOf(currentNode);

        if (childIndex == children.size() - INDEX_ADJUSTER || jumpedSinceLastTick) {
            return currentNode;
        } else {
            return children.get(childIndex + INDEX_ADJUSTER);
        }
    }

    /**
     * Jumps execution to the node with the given id.
     * @param nodeID the id of the target node
     */
    public void jumpTo(String nodeID) {
        Node target = nodesByID.get(nodeID);
        if (this.root == target) {
            resetTree();
            return;
        }
        handleSkippedNodes(target);
        currentNode = target;
        jumpedSinceLastTick = true;
    }

    private void handleSkippedNodes(Node target) {
        Node parent = target.getParent();
        if (parent == root) {
            for (Node child : root.getChildren()) {
                if (child == target) {
                    child.setNodeStatus(NodeStatus.ENTRY);
                    break;
                }
                child.setNodeStatus(simulateStatusForSkipped(root));
            }
            return;
        }
        handleSkippedNodes(parent);
        for (Node child : parent.getChildren()) {
            if (child == target) {
                break;
            }
            child.setNodeStatus(simulateStatusForSkipped(parent));
        }
    }
    private NodeStatus simulateStatusForSkipped(Node parent) {
        if (parent.getNodeType() == NodeType.SEQUENCE) {
            return NodeStatus.SUCCESS;
        } else {
            return NodeStatus.FAILURE;
        }
    }

    /**
     * Resets the entire behavior tree to its initial state.
     */
    public void resetTree() {
        resetSubtree(root);
    }

    /**
     * Updates the current node pointer.
     * @param newCurrent the new current node
     */
    public void setCurrentNode(Node newCurrent) {
        this.currentNode = newCurrent;
    }

    /**
     * Resets a subtree starting at the given node.
     * @param node the node to reset
     */
    private void resetSubtree(Node node) {
        node.resetState();
        for (Node child : node.getChildren()) {
            resetSubtree(child);
        }
    }

    /**
     * Gets the id from the root of this tree.
     * @return the root id
     */
    public String getRootID() {
        return root.getId();
    }

    /**
     * Adds a sibling node next to an existing node.
     * @param parent     the parent of the existing node
     * @param existingID the id of the existing node
     * @param newNode    the new node to insert
     */
    public void addSibling(Node parent, String existingID, Node newNode) {
        Node existing = nodesByID.get(existingID);
        parent.insertChildAfter(existing, newNode);
        newNode.registerSubtree(nodesByID);
    }

    /**
     * Finds a node by its id.
     * @param id the node id
     * @return the node
     */
    public Node findNodeByID(String id) {
        return nodesByID.get(id);
    }

    /**
     * Checks if the node from the given id exists in the tree.
     * @param nodeID the given id
     * @return true if it exists, false otherwise
     */
    public boolean hasNode(String nodeID) {
        for (Node node: nodesByID.values()) {
            if (node.getId().equals(nodeID)) {
                return true;
            }
        }
        return false;
    }
}
