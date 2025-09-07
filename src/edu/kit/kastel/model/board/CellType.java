package edu.kit.kastel.model.board;

/**
 * Represents the different kinds of cells that can appear on the board.
 * Each cell type has a unique character symbol.
 * @author ujsap
 */
public enum CellType {

    /**
     * A tree cell, represented by '#'.
     **/
    TREE('#'),

    /**
     * A leaf cell, represented by '*'.
     **/
    LEAF('*'),

    /**
     * A mushroom cell, represented by 'o'.
     **/
    MUSHROOM('o'),

    /**
     *  An empty cell, represented by '.'.
     **/
    EMPTY('.');

    private final char symbol;

    /**
     * Creates a new cell type with the given character symbol.
     * @param symbol the character used to represent this type
     */
    CellType(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the character symbol of this cell type.
     * @return the symbol as a char
     */
    public char toChar() {
        return this.symbol;
    }

    /**
     * Returns the name of this cell type in lower case.
     * @return a lower-case string representation
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * Converts a character to a cell type.
     * @param identifier the character symbol to match
     * @return the matching cell type, or null if no match is found
     */
    public static CellType fromChar(char identifier) {
        for (CellType type : values()) {
            if (type.symbol == identifier) {
                return type;
            }
        }
        return null;
    }
}
