package edu.kit.kastel.model.tree.nodes.composites;

import edu.kit.kastel.model.tree.TickContext;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeStatus;
import edu.kit.kastel.model.tree.nodes.NodeType;

/**
 * A parallel node in a behavior tree.
 * All children are executed sequentially.
 * The node succeeds if at least threshold children succeed.
 * Otherwise, it fails.
 * @author ujsap
 */
public class ParallelNode extends CompositeNode {
    private static final int INITIAL_SUCESS_COUNT = 0;
    private final int threshold;

    /**
     * Creates a new ParallelNode.
     * @param id        the node id
     * @param parent    the parent node
     * @param threshold the minimum number of successful children required for success
     */
    public ParallelNode(String id, Node parent, int threshold) {
        super(id, NodeType.PARALLEL, parent);
        this.threshold = threshold;
    }

    /**
     * Executes all children in parallel and checks against the success threshold.
     * @param tickContext the tick context
     * @return {@link NodeStatus#SUCCESS} if at least {@code threshold} children succeed,
     *         otherwise {@link NodeStatus#FAILURE}
     */
    @Override
    public NodeStatus tick(TickContext tickContext) {
        ensureEntry(tickContext);
        for (Node child : getChildren()) {
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
        }
        if (countSuccesses() < threshold) {
            setNodeStatus(NodeStatus.FAILURE);
        } else {
            setNodeStatus(NodeStatus.SUCCESS);
        }
        logExit(tickContext, getNodeStatus());
        return getNodeStatus();
    }

    /**
     * Counts the number of child nodes that have succeeded.
     * @return the number of successful children
     */
    private int countSuccesses() {
        int successCount = INITIAL_SUCESS_COUNT;
        for (Node child : getChildren()) {
            if (child.getNodeStatus() == NodeStatus.SUCCESS) {
                successCount++;
            }
        }
        return successCount;
    }
}
