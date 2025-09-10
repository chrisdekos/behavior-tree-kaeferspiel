package edu.kit.kastel.model.board;


import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.ArrayDeque;

/**
 * A two-dimensional board made of {@link CellType} cells.
 * Provides methods to check and update cells or find paths between positions.
 * @author Programmieren-Team
 * @author ujsap
 */
public class Board {

    private static final int FIRST_ROW = 0;
    private static final int FIRST_COLUMN = 0;
    private static final int MAXIMUM_NEIGHBORS_CAPACITY = 4;
    private final CellType[][] board;

    /**
     * Creates a new board as a copy of a given board.
     * @param board the initial cell layout
     */
    public Board(CellType[][] board) {
        this.board = new CellType[board.length][board[FIRST_ROW].length];
        for (int row = 0; row < board.length; row++) {
            this.board[row] = board[row].clone();
        }
    }

    /**
     * Returns the type of the cell at the given position.
     * @param position the position to look up
     * @return the cell type at that position
     */
    public CellType getCellType(Position position) {
        return board[position.row()][position.column()];
    }

    /**
     * Sets the type of the cell at the given position.
     * @param position the position to update
     * @param cellType the new cell type
     */
    public void setCellType(Position position, CellType cellType) {
        board[position.row()][position.column()] = cellType;
    }

    /**
     * Checks if the position is within board bounds.
     * @param position the position to check
     * @return true if inside the board, false otherwise
     */
    public boolean isWithinBounds(Position position) {
        return position.row() >= FIRST_ROW && position.row() < board.length
                && position.column() >= FIRST_COLUMN && position.column() < board[FIRST_ROW].length;
    }

    /**
     * Checks if the given position is empty.
     * @param position the position to check
     * @return true if the cell is empty
     */
    public boolean isEmpty(Position position) {
        return getCellType(position) == CellType.EMPTY;
    }

    /**
     * Returns all neighbors positions from a given position.
     * @param position the position to start from
     * @return a list of neighbors within the board
     */
    public List<Position> neighbors(Position position) {
        List<Position> neighbors = new ArrayList<>(MAXIMUM_NEIGHBORS_CAPACITY);
        for (Direction direction : Direction.values()) {
            Position neighbor = new Position(
                    position.column() + direction.getDeltaColumn(),
                    position.row() + direction.getDeltaRow());
            if (isWithinBounds(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    /**
     * Checks if there is a path of empty cells between two positions.
     * @param start the starting position
     * @param goal the target position
     * @return true if a path exists, false otherwise
     */
    public boolean existsPath(Position start, Position goal) {
        if (!isWithinBounds(start) || !isWithinBounds(goal)) {
            return false;
        }
        if (!isEmpty(start) || !isEmpty(goal)) {
            return false;
        }
        if (start.equals(goal)) {
            return true;
        }

        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            for (Position neighbor : neighbors(current)) {
                if (neighbor.equals(goal)) {
                    return true;
                }
                if (isEmpty(neighbor) && visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        return false;
    }

    /**
     * Creates a deep copy of this board.
     * @return a new Board with the same cell layout
     */
    public Board copy() {
        CellType[][] copyCells = new CellType[board.length][board[FIRST_ROW].length];
        for (int row = 0; row < board.length; row++) {
            copyCells[row] = board[row].clone();
        }
        return new Board(copyCells);
    }


    /**
     * Returns the number of rows of this board.
     * @return row count
     */
    public int rows() {
        return board.length;
    }

    /**
     * Returns the number of columns of this board.
     * @return column count
     */
    public int columns() {
        return board[FIRST_ROW].length;
    }
}
