package edu.kit.kastel.model.board;

import edu.kit.kastel.model.tree.BehaviorTree;

import java.util.Set;

/**
 * Represents a ladybug that can move, turn, and interact with objects on the board.
 * A ladybug has an id, a position, a facing direction and optionally a behavior tree.
 * @author ujsap
 */
public class Ladybug {
    private static final int NO_MOVEMENT_DELTA = 0;
    private final int id;
    private Position position;
    private Direction direction;
    private BehaviorTree behaviorTree;
    private boolean isActive;

    /**
     * Creates a new ladybug with the given id, starting position, and direction.
     * @param id the unique identifier of the ladybug
     * @param position the initial position on the board
     * @param direction the initial facing direction
     */
    public Ladybug(int id, Position position, Direction direction) {
        this.id = id;
        this.position = position;
        this.direction = direction;
        this.isActive = false;
    }

    /**
     * Gets the id of the ladybug.
     * @return the id of this ladybug
     **/
    public int getId() {
        return id;
    }

    /**
     * Gets the current position of the ladybug.
     * @return the current position of the ladybug
     **/
    public Position getPosition() {
        return position;
    }

    /**
     * Updates the position of the ladybug.
     * @param position the new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the facing direction of this ladybug.
     * @return the current facing direction
     **/
    public Direction getDirection() {
        return direction;
    }

    /**
     * Turns the ladybug to the left.
     **/
    public void turnLeft() {
        this.direction = this.direction.turnLeft();
    }

    /**
     * Turns the ladybug to the right.
     **/
    public void turnRight() {
        this.direction = this.direction.turnRight();
    }

    /**
     * Returns the position directly in front of the ladybug.
     * @return the position of the cell in front
     */
    public Position getCellInFront() {
        return new Position(
                position.column() + direction.getDeltaColumn(),
                position.row()  + direction.getDeltaRow()
        );
    }

    /**
     * Moves the ladybug to the given position.
     * @param newPosition the target position
     */
    private void moveTo(Position newPosition) {
        setPosition(newPosition);
    }

    /**
     * Tries to move one step forward. Handles mushrooms by pushing them,if possible.
     * Movement is blocked by trees, leaves or other ladybugs.
     * Front cell in the facing direction must be within bounds.
     * @param board the board
     * @param ladybugPositions positions of other ladybugs
     * @return true if the move succeeded, false otherwise
     */
    public boolean moveForward(Board board, Set<Position> ladybugPositions) {
        Position front = getCellInFront();
        if (!canEnterFront(board, ladybugPositions, front)) {
            return false;
        }
        switch (board.getCellType(front)) {
            case EMPTY -> {
                moveTo(front);
                return true;
            }
            case MUSHROOM -> {
                Position mushroomFront = new Position(front.column() + direction.getDeltaColumn(),
                        front.row() + direction.getDeltaRow());
                if (board.isWithinBounds(mushroomFront) && board.isEmpty(mushroomFront)) {
                    moveMushroom(board, front, mushroomFront);
                    moveTo(front);
                    return true;
                }
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean canEnterFront(Board board, Set<Position> ladybugPositions, Position front) {
        return board.isWithinBounds(front) && !ladybugPositions.contains(front);
    }

    private void moveMushroom(Board board, Position from, Position to) {
        board.setCellType(to, CellType.MUSHROOM);
        board.setCellType(from, CellType.EMPTY);
    }

    /**
     * Gets the behavior tree of this ladybug.
     * @return the behavior tree of this ladybug, or null if none is set
     **/
    public BehaviorTree getBehaviorTree() {
        return behaviorTree;
    }

    /**
     * Sets the behavior tree of the ladybug.
     * @param behaviorTree the new behavior tree
     */
    public void setBehaviorTree(BehaviorTree behaviorTree) {
        this.behaviorTree = behaviorTree;
    }

    /**
     * Places a leaf in the cell in front of the ladybug if it is empty.
     * @param board the board
     * @return true if a leaf was placed, false otherwise
     */
    public boolean placeLeafFront(Board board) {
        return updateFrontCell(board, CellType.EMPTY, CellType.LEAF);
    }

    /**
     * Takes a leaf from the cell in front of the ladybug if one exists.
     * @param board the board
     * @return true if a leaf was taken, false otherwise
     */
    public boolean takeLeafFront(Board board) {
        return updateFrontCell(board, CellType.LEAF, CellType.EMPTY);
    }

    private boolean updateFrontCell(Board board, CellType expected, CellType target) {
        Position inFront = getCellInFront();
        if (!board.isWithinBounds(inFront)) {
            return false;
        }
        if (board.getCellType(inFront) != expected) {
            return false;
        }
        board.setCellType(inFront, target);
        return true;
    }

    /**
     * Makes the ladybug fly to a new position if it is empty and within bounds.
     * The direction after flying is updated.
     * @param board the board
     * @param goal the target position
     * @param ladybugPositions positions of other ladybugs
     * @return true if the flight succeeded, false otherwise
     */
    public boolean fly(Board board, Position goal, Set<Position> ladybugPositions) {
        if (!board.isWithinBounds(goal) || !board.isEmpty(goal) || ladybugPositions.contains(goal)) {
            return false;
        }
        this.direction = calculateDirectionAfterFly(goal);
        moveTo(goal);
        return true;
    }

    private Direction calculateDirectionAfterFly(Position goal) {
        int deltaX = goal.column() - this.position.column();
        int deltaY = goal.row() - this.position.row();

        if (deltaY == NO_MOVEMENT_DELTA && deltaX == NO_MOVEMENT_DELTA) {
            return this.direction;
        }
        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            return deltaX > NO_MOVEMENT_DELTA ? Direction.RIGHT : Direction.LEFT;
        } else {
            return deltaY < NO_MOVEMENT_DELTA ? Direction.UP : Direction.DOWN;
        }
    }

    /**
     * Checks if this ladybug is active.
     * @return true if the ladybug is active.
     **/
    public boolean getIfActive() {
        return this.isActive;
    }


    /**
     * Creates a full copy of this ladybug with the same id, position and direction.
     * @return a new identical Ladybug instance
     */
    public Ladybug copy() {
        return new Ladybug(this.id, this.position, this.direction);
    }



    /**
     * Setter for the isActive attrivute.
     * @param value true to activate, false to deactivate
     */
    public void setActive(boolean value) {
        this.isActive = value;
    }
}
