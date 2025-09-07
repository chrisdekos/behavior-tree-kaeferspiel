package edu.kit.kastel.model.parsing;

import edu.kit.kastel.model.exceptions.BoardParserException;
import edu.kit.kastel.model.board.Board;
import edu.kit.kastel.model.board.Ladybug;
import edu.kit.kastel.model.board.CellType;
import edu.kit.kastel.model.board.Direction;
import edu.kit.kastel.model.board.Position;

import java.util.List;

/**
 * Parses textual representations of a board into Board objects.
 * A board is described line by line, with each character representing
 * either a CellType or a Ladybug.
 * When a ladybug symbol is encountered, a new Ladybug instance is created and added to the given list.
 * @author ujsap
 */
public final class BoardParser {

    private static final int START_ID = 1;
    private static final String INVALID_CELL_TYPE_ERROR = "invalid cell type %s";
    private static final String NO_LADYBUG_ON_BOARD_ERROR = "no ladybug on board";

    /**
     * Reads a board from the given text lines.
     * @param lines    the input lines, meanwhile each one represents a row of the board.
     * @param ladybugs the list where all parsed ladybugs are added
     * @return a board with the parsed cells
     * @throws BoardParserException if an invalid symbol is found or if the board contains no ladybugs
     */
    public Board parseBoard(List<String> lines, List<Ladybug> ladybugs) throws BoardParserException {
        final int rows = lines.size();
        final int columns = lines.getFirst().length();
        final CellType[][] board = new CellType[rows][columns];

        int ladybugID = START_ID;

        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            final String row = lines.get(rowIndex);
            for (int column = 0; column < columns; column++) {
                final char character = row.charAt(column);

                Direction direction = Direction.fromChar(character);
                if (direction != null) {
                    ladybugs.add(new Ladybug(ladybugID++, new Position(column, rowIndex), direction));
                    board[rowIndex][column] = CellType.EMPTY;
                    continue;
                }

                CellType cellType = CellType.fromChar(character);
                if (cellType != null) {
                    board[rowIndex][column] = cellType;
                    continue;
                }

                throw new BoardParserException(INVALID_CELL_TYPE_ERROR.formatted(character));
            }
        }

        if (ladybugs.isEmpty()) {
            throw new BoardParserException(NO_LADYBUG_ON_BOARD_ERROR);
        }

        return new Board(board);
    }
}
