package edu.kit.kastel.model.board;

/**
 * Immutable position on the board, identified by a column and a row.
 * @param column the column index.
 * @param row    the row index.
 * @author ujsap
 */
public record Position(int column, int row) {
}
