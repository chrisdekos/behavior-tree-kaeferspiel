package edu.kit.kastel.view.util;

import edu.kit.kastel.model.board.Board;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.board.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for preparing output of boards, trees, and positions.
 * Provides helper methods for rendering boards with borders and ladybugs,
 * formatting positions with view coordinates and preparing, verbatim prints of board and trees.
 * @author ujsap
 */

public final class PrintHelpers {

    private static final String THREE_PARTS_UPPER_BORDER_FORMAT = "%s%s%s";
    private static final char BORDER_CORNER = '+';
    private static final char BORDER_HORIZONTAL = '-';
    private static final char BORDER_VERTICAL = '|';
    private static final int BORDER_EXTRA_COLUMNS = 2;
    private static final String VIEW_POSITION_FORMAT = "(%d, %d)";
    private static final int VIEW_INDEX_SHIFT = 1;


    private PrintHelpers() {
        //Utility class
    }

    /**
     * Joins the given lines into a single string separated by system line breaks.
     * @param lines the lines to join
     * @return a single string with all lines
     */
    public static String prepareVerbatimPrint(List<String> lines) {
        return String.join(System.lineSeparator(), lines);
    }

    /**
     * Renders the given board with borders and ladybugs and returns it as a printable string.
     * @param board    the board to render
     * @param ladybugs the list of ladybugs to display on the board
     * @return a string representation of the rendered board
     */
    public static String prepareRenderedBoard(Board board, List<Ladybug> ladybugs) {
        return String.join(System.lineSeparator(), renderWithBorders(board, ladybugs));
    }

    /**
     * Converts a {@link Position} to  a position with view coordinates.
     * @param position the position to convert
     * @return the formatted position string
     */
    public static String toViewPosition(Position position) {
        return String.format(VIEW_POSITION_FORMAT, position.column() + VIEW_INDEX_SHIFT,
                position.row() + VIEW_INDEX_SHIFT);
    }


    private static List<String> renderWithBorders(Board board, List<Ladybug> ladybugs) {
        int rows = board.rows();
        int columns = board.columns();

        Map<Position, Character> ladybugByPosition = new HashMap<>();
        for (Ladybug ladybug : ladybugs) {
            ladybugByPosition.put(ladybug.getPosition(), ladybug.getDirection().toChar());
        }

        List<String> outputBoard = new ArrayList<>(rows + 2);
        String border = THREE_PARTS_UPPER_BORDER_FORMAT.formatted(
                BORDER_CORNER, String.valueOf(BORDER_HORIZONTAL).repeat(columns), BORDER_CORNER);
        outputBoard.add(border);

        for (int row = 0; row < rows; row++) {
            StringBuilder stringBuilder = new StringBuilder(
                    columns + BORDER_EXTRA_COLUMNS).append(BORDER_VERTICAL);
            for (int column = 0; column < columns; column++) {
                Position position = new Position(column, row);
                char character = ladybugByPosition.getOrDefault(position, board.getCellType(position).toChar());
                stringBuilder.append(character);
            }
            stringBuilder.append(BORDER_VERTICAL);
            outputBoard.add(stringBuilder.toString());

        }

        outputBoard.add(border);
        return outputBoard;
    }
}
