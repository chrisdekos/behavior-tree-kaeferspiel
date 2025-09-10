package edu.kit.kastel.model.board;

/**
 * Represents the four directions that can be used to move across the board.
 * Each direction has a character symbol and row/column deltas that describe the movement.
 * @author ujsap
 */
public enum Direction {

    /**
     * Upwards direction, represented by '^'.
     **/
    UP('^', 0, -1),

    /**
     * Rightwards direction, represented by '>'.
     **/
    RIGHT('>', 1, 0),

    /**
     * Downwards direction, represented by 'v'.
     **/
    DOWN('v', 0, 1),

    /**
     * Leftwards direction, represented by '<'.
     **/
    LEFT('<', -1, 0);

    private final char symbol;
    private final int deltaRow;
    private final int deltaColumn;

    /**
     * Creates a direction with the given symbol and row/column deltas.
     * @param symbol the character symbol
     * @param deltaColumn the change in column when moving in this direction
     * @param deltaRow the change in row when moving in this direction
     */
    Direction(char symbol, int deltaColumn, int deltaRow) {
        this.symbol = symbol;
        this.deltaRow = deltaRow;
        this.deltaColumn = deltaColumn;
    }

    /**
     * Returns the character symbol of this direction.
     * @return the symbol as a char
     */
    public char toChar() {
        return this.symbol;
    }

    /**
     * Returns the row delta of this direction.
     * @return the row delta
     */
    public int getDeltaRow() {
        return this.deltaRow;
    }

    /**
     * Returns the column delta of this direction.
     * @return the column delta
     */
    public int getDeltaColumn() {
        return this.deltaColumn;
    }

    /**
     * Returns the direction that results from turning left.
     * @return the new direction after turning left
     */
    public Direction turnLeft() {
        return switch (this) {
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            case RIGHT -> UP;
        };
    }

    /**
     * Returns the direction that results from turning right.
     * @return the new direction after turning right
     */
    public Direction turnRight() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }

    /**
     * Converts a character symbol to a direction.
     * @param identifier the character symbol
     * @return the matching direction, or null if no match exists
     */
    public static Direction fromChar(char identifier) {
        for (Direction direction : values()) {
            if (direction.symbol == identifier) {
                return direction;
            }
        }
        return null;
    }
}
