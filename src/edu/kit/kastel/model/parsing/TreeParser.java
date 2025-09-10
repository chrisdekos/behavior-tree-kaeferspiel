package edu.kit.kastel.model.parsing;

import edu.kit.kastel.model.tree.BehaviorTree;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.tree.nodes.NodeType;
import edu.kit.kastel.model.tree.nodes.composites.FallbackNode;
import edu.kit.kastel.model.tree.nodes.composites.ParallelNode;
import edu.kit.kastel.model.tree.nodes.composites.SequenceNode;
import edu.kit.kastel.model.tree.nodes.leafs.actions.ActionType;
import edu.kit.kastel.model.tree.nodes.leafs.actions.MoveNode;
import edu.kit.kastel.model.tree.nodes.leafs.actions.PlaceLeafNode;
import edu.kit.kastel.model.tree.nodes.leafs.actions.TurnLeftNode;
import edu.kit.kastel.model.tree.nodes.leafs.actions.TurnRightNode;
import edu.kit.kastel.model.tree.nodes.leafs.actions.TakeLeafNode;
import edu.kit.kastel.model.tree.nodes.leafs.actions.FlyNode;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.ConditionType;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.AtEdgeNode;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.LeafFrontNode;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.TreeFrontNode;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.ExistsPathBetweenNode;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.ExistsPathToNode;
import edu.kit.kastel.model.tree.nodes.leafs.conditions.MushroomFrontNode;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.board.Position;
import edu.kit.kastel.model.exceptions.TreeParserException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses behavior trees written in a mermaid syntax notation.
 * Each tree starts with the header flowchart TD, followed by edges
 * connecting nodes. Nodes can represent composites, actions, or conditions.
 * The parser validates structure and creates a behavior Tree for each ladybug.
 * @author ujsap
 */
public final class TreeParser {

    private static final String REGEX_GROUP_CONDITION = "condition";
    private static final String UNKNOWN_CONDITION_ERROR = "unknown condition: ";
    private static final String LESS_LADYBUGS_THAN_TREES_ERROR = "there can't be less ladybugs than trees";
    private static final String FLOWCHART_TD_ERROR = "format does not equal 'flowchart TD' ";
    private static final String INVALID_EDGE_ERROR = "invalid edge syntax: %s ";
    private static final String CAN_NOT_BE_PARENT_ERROR = " %s type can not be parent";
    private static final String INVALID_NODE_FORMAT = "invalid node format: %s ";
    private static final String EMPTY_TEXT = "";
    private static final int LIMIT_TO_SPLIT = 2;
    private static final String INVALID_NODE_TOKEN_ERROR = "invalid node token: %s";
    private static final String REGEX_GROUP_ID = "id";
    private static final String REGEX_GROUP_REPRESENTATION = "representation";
    private static final String CONTAINS_NO_ACTION_ERROR = "tree must contain at least one action";
    private static final String REGEX_GROUP_INNER = "inner";
    private static final String WRONG_TYPE_FORMAT_ERROR = " node type format is not correct: ";
    private static final String UNKNOWN_ACTION_ERROR = "unknown action: ";
    private static final String INVALID_FLY_COORDINATES_ERROR = "invalid fly coordinates: ";
    private static final String REGEX_GROUP_PARENT = "parent";
    private static final String REGEX_GROUP_CHILD = "child";
    private static final String OVERRIDING_A_NODE_ERROR = "overriding a node is not possible";
    private static final String INVALID_PARALLEL_THRESHOLD_ERROR = "invalid parallel threshold: ";
    private static final String REGEX_GROUP_NUMBER = "number";
    private static final int INITIAL_TREE_COUNT = 0;
    private static final int COMMAND_ARGUMENT_INDEX = 1;
    private static final int REGEX_GROUP_ROW = 1;
    private static final int REGEX_GROUP_COLUMN = 2;
    private static final int INPUT_TO_MODEL_ADJUSTER = 1;
    private static final String STANDARD_EMPTY_TEXT_VALUE = "";
    private static final int FIRST_PART_INDEX = 0;

    // regex for one or more whitespaces
    private static final String WHITESPACE_REGEX = "\\s+";

    // matches exactly the tree header "flowchart TD"
    private static final Pattern TREE_HEADER_REGEX = Pattern.compile("^flowchart TD$");

    // matches a node type representation: either "[...]" or "([...])"
    private static final String NODE_TYPE_REGEX = "\\(\\[[^]]+]\\)|\\[[^]]+]";

    // matches a valid node id
    private static final String ID = "[^\\s\\[(]+";

    // matches an edge line, parent --> child,
    // both parent and child have a mandatory id, meanwhile only child has a mandatory representation
    private static final Pattern TREE_EDGE_REGEX = Pattern.compile(
            "^ {4}(?<parent>" + ID + "(?:" + NODE_TYPE_REGEX + ")?) --> (?<child>" + ID
                    + "(?:" + NODE_TYPE_REGEX + "))$");

    // matches a single side of an edge. Includes id and an optional representation
    private static final Pattern SIDE_TOKEN_REGEX =
            Pattern.compile("^(?<id>" + ID + ")(?<representation>" + NODE_TYPE_REGEX + ")?$");

    // matches a condition representation like "([condition])"
    private static final Pattern CONDITION_REGEX_FORMAT = Pattern.compile(
            "^\\(\\[(?<condition>[^]]+?)]\\)$");

    // matches valid coordinates for positions
    private static final Pattern COORDINATES_REGEX =
            Pattern.compile("\\s*(-?\\d+)\\s*,\\s*(-?\\d+)");

    // matches brackets and captures their inner text
    private static final Pattern BRACKET_REGEX_FORMAT = Pattern.compile("^\\[(?<inner>[^]]*?)]$");

    // matches a parallel node threshold like
    private static final Pattern PARALLEL_REGEX_FORMAT = Pattern.compile("^=(?<number>[1-9]\\d*)>$");

    // matches a fallback node marker
    private static final Pattern FALLBACK_REGEX_FORMAT = Pattern.compile("^\\?$");

    // matches a sequence node marker
    private static final Pattern SEQUENCE_REGEX_FORMAT = Pattern.compile("^->$");

    private int treeCount;
    private Node currentRoot;
    private boolean hasAction;
    private Map<String, Node> nodes;

    /**
     * Parses one or more behavior trees from the given lines.
     * @param lines    the input text lines
     * @param ladybugs the ladybugs that will be assigned trees
     * @return a list of behavior trees
     * @throws TreeParserException if the input is invalid
     */
    public List<BehaviorTree> parse(List<String> lines, List<Ladybug> ladybugs) throws TreeParserException {
        List<BehaviorTree> behaviorTrees = new ArrayList<>();
        boolean inTree = false;
        boolean edgeExists = false;

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            if (TREE_HEADER_REGEX.matcher(line).matches()) {
                if (inTree) {
                    behaviorTrees.add(createCurrentTree());
                }
                treeCount++;
                if (treeCount > ladybugs.size()) {
                    throw new TreeParserException(LESS_LADYBUGS_THAN_TREES_ERROR);
                }

                this.nodes = new LinkedHashMap<>();
                inTree = true;
                edgeExists = false;
                hasAction = false;
                currentRoot = null;
                continue;
            }

            if (!inTree) {
                throw new TreeParserException(FLOWCHART_TD_ERROR);
            }

            Matcher edgeMatcher = TREE_EDGE_REGEX.matcher(line);
            if (!edgeMatcher.matches()) {
                throw new TreeParserException(String.format(INVALID_EDGE_ERROR, line));
            }

            String left = edgeMatcher.group(REGEX_GROUP_PARENT).trim();
            String right = edgeMatcher.group(REGEX_GROUP_CHILD).trim();

            Node parent = parseSide(left, this.nodes);
            Node child = parseSide(right, this.nodes);

            if (!parent.getNodeType().isComposite()) {
                throw new TreeParserException(String.format(CAN_NOT_BE_PARENT_ERROR, parent.getNodeType()));
            }

            if (!edgeExists) {
                currentRoot = parent;
                parent.setParent(parent);
                edgeExists = true;
            }
            parent.addChild(child);
        }

        if (inTree) {
            behaviorTrees.add(createCurrentTree());
        }
        treeCount = INITIAL_TREE_COUNT;
        return behaviorTrees;
    }

    private Node parseSide(String text, Map<String, Node> nodesByID) throws TreeParserException {
        Matcher sideNodeMatcher = SIDE_TOKEN_REGEX.matcher(text);
        if (!sideNodeMatcher.matches()) {
            throw new TreeParserException(String.format(INVALID_NODE_FORMAT, text));
        }

        String id = sideNodeMatcher.group(REGEX_GROUP_ID);
        String representationGroup = sideNodeMatcher.group(REGEX_GROUP_REPRESENTATION);
        String representation = (representationGroup != null) ? representationGroup : EMPTY_TEXT;

        Node existing = nodesByID.get(id);
        if (existing != null) {
            if (!representation.isEmpty()) {
                throw new TreeParserException(OVERRIDING_A_NODE_ERROR);
            }
            return existing;
        }

        NodeType type = nodeTypeOf(representation);
        Node created = createNode(id, type, representation);
        nodesByID.put(id, created);
        return created;
    }

    private Node createNode(String id, NodeType type, String representation) throws TreeParserException {
        return switch (type) {
            case FALLBACK -> new FallbackNode(id, null);
            case SEQUENCE -> new SequenceNode(id, null);
            case PARALLEL -> {
                int threshold = getThreshold(representation);
                yield new ParallelNode(id, null, threshold);
            }
            case ACTION -> {
                hasAction = true;
                yield createActionNode(id, extractInner(representation, type));
            }
            case CONDITION -> createConditionNode(id, extractInner(representation, type));
        };
    }

    private int getThreshold(String representation) throws TreeParserException {
        Matcher bracketsParallel = BRACKET_REGEX_FORMAT.matcher(representation);
        if (!bracketsParallel.matches()) {
            throw new TreeParserException(INVALID_PARALLEL_THRESHOLD_ERROR + representation);
        }
        String parallelText = bracketsParallel.group(REGEX_GROUP_INNER).trim();
        Matcher number = PARALLEL_REGEX_FORMAT.matcher(parallelText);
        if (!number.matches()) {
            throw new TreeParserException(INVALID_PARALLEL_THRESHOLD_ERROR + representation);
        }
        return Integer.parseInt(number.group(REGEX_GROUP_NUMBER));
    }

    private Node createActionNode(String id, String representation) throws TreeParserException {
        String[] parts = representation.split(WHITESPACE_REGEX, LIMIT_TO_SPLIT);
        String key = parts[FIRST_PART_INDEX];
        String args = (parts.length > COMMAND_ARGUMENT_INDEX)
                ? parts[COMMAND_ARGUMENT_INDEX].trim() : STANDARD_EMPTY_TEXT_VALUE;

        ActionType type = ActionType.fromString(key);
        if (type == null) {
            throw new TreeParserException(UNKNOWN_ACTION_ERROR + key);
        }
        return switch (type) {
            case MOVE -> new MoveNode(id, null);
            case TURN_LEFT -> new TurnLeftNode(id, null);
            case TURN_RIGHT -> new TurnRightNode(id, null);
            case TAKE_LEAF -> new TakeLeafNode(id, null);
            case PLACE_LEAF -> new PlaceLeafNode(id, null);
            case FLY -> {
                Position position = toPosition(args);
                if (position == null) {
                    throw new TreeParserException(INVALID_FLY_COORDINATES_ERROR + args);
                }
                yield new FlyNode(id, null, position);
            }
        };
    }

    private Node createConditionNode(String id, String representation) throws TreeParserException {
        ConditionType type = ConditionType.fromRepresentation(representation);
        if (type == null) {
            throw new TreeParserException(UNKNOWN_CONDITION_ERROR + representation);
        }
        return switch (type) {
            case AT_EDGE -> new AtEdgeNode(id, null);
            case LEAF_FRONT -> new LeafFrontNode(id, null);
            case TREE_FRONT -> new TreeFrontNode(id, null);
            case MUSHROOM_FRONT -> new MushroomFrontNode(id, null);
            case EXISTS_PATH_TO -> {
                List<Position> position = parsePositions(representation);
                yield new ExistsPathToNode(id, null, position.getFirst());
            }
            case EXISTS_PATH_BETWEEN -> {
                List<Position> positions = parsePositions(representation);
                yield new ExistsPathBetweenNode(id, null, positions.getFirst(), positions.getLast());
            }
        };
    }

    private NodeType nodeTypeOf(String representation) throws TreeParserException {
        String trimmed = representation.trim();

        if (CONDITION_REGEX_FORMAT.matcher(trimmed).matches()) {
            return NodeType.CONDITION;
        }

        Matcher bracketMatcher = BRACKET_REGEX_FORMAT.matcher(trimmed);
        if (!bracketMatcher.matches()) {
            throw new TreeParserException(WRONG_TYPE_FORMAT_ERROR + representation);
        }

        String inner = bracketMatcher.group(REGEX_GROUP_INNER).trim();
        if (FALLBACK_REGEX_FORMAT.matcher(inner).matches()) {
            return NodeType.FALLBACK;
        }
        if (PARALLEL_REGEX_FORMAT.matcher(inner).matches()) {
            return NodeType.PARALLEL;
        }
        if (SEQUENCE_REGEX_FORMAT.matcher(inner).matches()) {
            return NodeType.SEQUENCE;
        }

        return NodeType.ACTION;
    }

    private String extractInner(String representation, NodeType nodeType) {
        return switch (nodeType) {
            case ACTION -> {
                Matcher action = BRACKET_REGEX_FORMAT.matcher(representation);
                yield action.matches() ? action.group(REGEX_GROUP_INNER) : EMPTY_TEXT;
            }
            case CONDITION -> {
                Matcher condition = CONDITION_REGEX_FORMAT.matcher(representation);
                yield condition.matches() ? condition.group(REGEX_GROUP_CONDITION) : EMPTY_TEXT;
            }
            default -> EMPTY_TEXT;
        };
    }

    private Position toPosition(String token) {
        Matcher positionMatcher = COORDINATES_REGEX.matcher(token);
        if (!positionMatcher.matches()) {
            return null;
        }
        int row = Integer.parseInt(positionMatcher.group(REGEX_GROUP_ROW)) - INPUT_TO_MODEL_ADJUSTER;
        int col = Integer.parseInt(positionMatcher.group(REGEX_GROUP_COLUMN)) - INPUT_TO_MODEL_ADJUSTER;
        return new Position(row, col);
    }


    private static List<Position> parsePositions(String text) {
        Matcher coordinatesMatcher = COORDINATES_REGEX.matcher(text);
        List<Position> positions = new ArrayList<>();
        while (coordinatesMatcher.find()) {
            int row = Integer.parseInt(coordinatesMatcher.group(REGEX_GROUP_ROW)) - INPUT_TO_MODEL_ADJUSTER;
            int col = Integer.parseInt(coordinatesMatcher.group(REGEX_GROUP_COLUMN)) - INPUT_TO_MODEL_ADJUSTER;
            positions.add(new Position(row, col));
        }
        return positions;
    }


    private BehaviorTree createCurrentTree() throws TreeParserException {
        if (!hasAction) {
            throw new TreeParserException(CONTAINS_NO_ACTION_ERROR);
        }
        return new BehaviorTree(currentRoot);
    }

    /**
     * Parses a single node definition from its textual token.
     * The token must follow the expected format.
     * A new Node is created with the given id and type.
     * @param token the textual node representation
     * @return the parsed node
     * @throws TreeParserException if the token is invalid or the node type cannot be determined
     */
    public Node parseSingleNode(String token) throws TreeParserException {
        Matcher nodeMatcher = SIDE_TOKEN_REGEX.matcher(token);
        if (!nodeMatcher.matches()) {
            throw new TreeParserException(String.format(INVALID_NODE_TOKEN_ERROR, token));
        }

        String id = nodeMatcher.group(REGEX_GROUP_ID);
        String representation = nodeMatcher.group(REGEX_GROUP_REPRESENTATION);
        NodeType type = nodeTypeOf(representation);

        return createNode(id, type, representation);
    }
}
