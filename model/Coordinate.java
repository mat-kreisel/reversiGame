package reversi.model;

import java.util.Objects;

/**
 * Represents a coordinate (row and column) on the Reversi game board.
 * <p>
 * This class is used to represent the position of a move on the board. It
 * stores the row and column values, and provides methods for accessing and
 * comparing coordinates.
 * </p>
 */
public class Coordinate {

    // The row of the coordinate (0-based index).
    private final int row;

    // The column of the coordinate (0-based index).
    private final int col;

    /**
     * Constructs a new {@code Coordinate} object with the specified row and
     * column.
     *
     * @param row The row index (0-based).
     * @param col The column index (0-based).
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row of this coordinate.
     *
     * @return The row index (0-based).
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of this coordinate.
     *
     * @return The column index (0-based).
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns a string representation of the coordinate in the form of "
     * (row, col)",
     * where the row and column are 1-based (i.e., starting from 1).
     *
     * @return A string representation of the coordinate.
     */
    @Override
    public String toString() {
        return "(" + (row + 1) + ", " + (col + 1) + ")";
    }

    /**
     * Compares this coordinate to another object for equality. Two
     * coordinates are considered equal if their row and column values are
     * the same.
     *
     * @param o The object to compare this coordinate to.
     * @return {@code true} if the coordinates are equal, {@code false}
     * otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return row == that.row && col == that.col;
    }

    /**
     * Returns a hash code value for this coordinate. The hash code is
     * computed using the row and column values.
     *
     * @return A hash code value for this coordinate.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
