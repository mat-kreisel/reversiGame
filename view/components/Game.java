package reversi.view.components;

import reversi.model.Board;
import reversi.model.Coordinate;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.MouseListener;

/**
 * The {@code Game} class represents the game board in the Reversi GUI.
 * It is a JPanel containing a grid of {@link Slot} components, each
 * representing a cell on the board. This class is responsible for managing
 * the board's layout, adding and updating slots, and handling user input
 * through mouse listeners.
 */
public class Game extends JPanel {
    private static final int GRID_GAP = 2; // Gap between grid cells
    private static final int MARGIN = 5; // Margin for each slot
    private final Slot[][] board;

    /**
     * Initializes the game board as a grid of slots.
     * The layout is configured as a GridLayout with horizontal and vertical
     * gaps between slots.
     */
    public Game() {
        board = new Slot[Board.SIZE][Board.SIZE];
        GridLayout grid = new GridLayout(Board.SIZE, Board.SIZE);
        grid.setVgap(GRID_GAP);
        grid.setHgap(GRID_GAP);
        this.setLayout(grid);
        this.setBackground(Color.BLACK);
    }

    /**
     * Adds a slot at the specified position on the board with the given color.
     * The slot represents a cell in the game board and is placed into the
     * grid layout.
     *
     * @param position the coordinate of the slot to be added
     * @param color the color of the tile in the slot (used for human or bot
     *              player)
     */
    public void addSlot(Coordinate position, Color color) {
        int row = position.getRow();
        int col = position.getCol();
        // Create and add a new Slot at the given position
        board[row][col] = new Slot(color, position);
        board[row][col].setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN,
                MARGIN));
        this.add(board[row][col]);
    }

    /**
     * Updates the color of the slot at the specified position on the board.
     *
     * @param position the coordinate of the slot to be updated
     * @param color the new color to set for the slot
     */
    public void updateSlot(Coordinate position, Color color) {
        board[position.getRow()][position.getCol()].updateColor(color);
    }

    /**
     * Adds a {@link MouseListener} to all slots on the game board. This
     * allows for user interaction, such as mouse clicks on the individual
     * slots.
     *
     * @param mouseListener the mouse listener to be added to all slots
     */
    public void addListener(MouseListener mouseListener) {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board[i][j].addListener(mouseListener);
            }
        }
    }
}

