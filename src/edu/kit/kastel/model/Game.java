package edu.kit.kastel.model;

import edu.kit.kastel.model.tree.BehaviorTree;
import edu.kit.kastel.model.tree.Trace;
import edu.kit.kastel.model.tree.nodes.Node;
import edu.kit.kastel.model.board.Board;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.board.Position;
import edu.kit.kastel.model.exceptions.BoardParserException;
import edu.kit.kastel.model.exceptions.TreeParserException;
import edu.kit.kastel.model.parsing.BoardParser;
import edu.kit.kastel.model.parsing.TreeParser;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Central class for managing the game state.
 * A game consists of a {@link Board}, a set of {@link Ladybug} and behavior trees that control their actions.
 * This class provides methods to load boards and trees, run next actions and manipulate the game state.
 * @author ujsap
 */
public class Game {

    private static final int ADJUST_INDEX_NUMBER = 1;
    private final List<Ladybug> ladybugs = new ArrayList<>();

    private Board board;
    private Board initialBoard;
    private final BoardParser boardParser;
    private final TreeParser treeParser;
    private boolean boardLoaded;
    private boolean treesLoaded;

    /**
     * Creates a new game with its own board and tree parsers.
     */
    public Game() {
        this.boardParser = new BoardParser();
        this.treeParser = new TreeParser();
    }

    /**
     * Gets the board.
     * @return the current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Loads a board from its textual representation and initializes ladybugs.
     * It saves a second board instance, in order to reset the board later on, in case trees are loaded again.
     * @param lines the lines representing the board
     * @throws BoardParserException if the board could not be parsed
     */
    public void loadBoard(List<String> lines) throws BoardParserException {
        ladybugs.clear();
        Board parsedBoard = boardParser.parseBoard(lines, ladybugs);
        setBoard(parsedBoard);
        initialBoard = parsedBoard;
        setBoardLoaded();
        setTreesLoaded(false);
    }

    /**
     * Loads behavior trees and assigns them to the ladybugs.
     * It sets the board to its initial state.
     * @param treeFiles the textual tree descriptions
     * @throws TreeParserException if a tree cannot be parsed
     */
    public void loadTrees(List<String> treeFiles) throws TreeParserException {
        List<BehaviorTree> behaviorTrees = treeParser.parse(treeFiles, ladybugs);
        for (int i = 0; i < behaviorTrees.size(); i++) {
            Ladybug ladybug = ladybugs.get(i);
            ladybug.setBehaviorTree(behaviorTrees.get(i));
            ladybug.setActive(true);
        }
        setBoard(initialBoard);
        setTreesLoaded(true);
    }

    /**
     * Executes one tick for the specified ladybug.
     * @param ladybugID the id of the ladybug
     * @return the trace of the executed action
     */
    public Trace singleNextAction(int ladybugID) {
        Set<Position> positions = getLadybugPositions(ladybugs);
        return ladybugs.get(ladybugID - ADJUST_INDEX_NUMBER)
                .getBehaviorTree().tick(ladybugs.get(ladybugID - ADJUST_INDEX_NUMBER), board, positions);
    }

    /**
     * Lists all ladybugs.
     * @return an unmodifiable view of all ladybugs
     */
    public List<Ladybug> listLadybugs() {
        return Collections.unmodifiableList(this.ladybugs);
    }

    /**
     * Collects the positions of the given ladybugs.
     * @param ladybugs the list of ladybugs
     * @return a set of their positions
     */
    public Set<Position> getLadybugPositions(List<Ladybug> ladybugs) {
        Set<Position> positions = new HashSet<>();
        for (Ladybug ladybug : ladybugs) {
            positions.add(ladybug.getPosition());
        }
        return positions;
    }

    /**
     * Returns a ladybug by its id.
     * @param ladybugID the id
     * @return the ladybug
     */
    public Ladybug getLadybug(int ladybugID) {
        return ladybugs.get(ladybugID - ADJUST_INDEX_NUMBER);
    }

    /**
     * Resets the behavior tree of the specified ladybug.
     * @param id the ladybug id (1-based)
     */
    public void resetTree(int id) {
        ladybugs.get(id - ADJUST_INDEX_NUMBER).getBehaviorTree().resetTree();
    }

    /**
     * Moves execution in a ladybug's tree to a specific node.
     * @param ladybugID the ladybug id
     * @param nodeID    the target node id
     */
    public void jumpTo(int ladybugID, String nodeID) {
        ladybugs.get(ladybugID - ADJUST_INDEX_NUMBER).getBehaviorTree().jumpTo(nodeID);
    }

    /**
     * Adds a sibling node next to an existing node in a ladybug's tree.
     * @param ladybugID             the ladybug id
     * @param existingNodeID        the node id to add next to
     * @param newNodeRepresentation the textual representation of the new node
     * @throws TreeParserException if the new node cannot be parsed
     */
    public void addSibling(int ladybugID, String existingNodeID, String newNodeRepresentation) throws TreeParserException {
        Node newNode = treeParser.parseSingleNode(newNodeRepresentation);
        Node parent = getLadybug(ladybugID).getBehaviorTree().findNodeByID(existingNodeID).getParent();
        ladybugs.get(ladybugID - ADJUST_INDEX_NUMBER).getBehaviorTree().addSibling(parent, existingNodeID, newNode);
    }

    /**
     * Returns the root node of a ladybug's tree.
     * @param ladybugID the ladybug id
     * @return the root node
     */
    public Node head(int ladybugID) {
        return ladybugs.get(ladybugID - ADJUST_INDEX_NUMBER).getBehaviorTree().head();
    }

    /**
     * Check if the board is loaded.
     * @return true if a board has been loaded
     */
    public boolean isBoardLoaded() {
        return boardLoaded;
    }

    private boolean areTreesLoaded() {
        return treesLoaded;
    }

    private void setBoardLoaded() {
        boardLoaded = true;
    }

    private void setTreesLoaded(boolean value) {
        this.treesLoaded = value;
    }

    private void setBoard(Board newBoard) {
        this.board = newBoard;
    }

    /**
     * Checks if further actions except load board and load trees can be used.
     * @return true if both board and trees are loaded
     */
    public boolean allActionsEnabled() {
        return areTreesLoaded() && isBoardLoaded();
    }
}
