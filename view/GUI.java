package reversi.view;

import reversi.model.Board;
import reversi.model.Coordinate;
import reversi.view.components.Game;
import reversi.view.components.Menu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Container;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * The GUI class represents the graphical user interface for the Reversi game.
 * It extends {@link JFrame}. The class contains and is responsible for
 * displaying the game board and the menu.
 */
public class GUI extends JFrame {
    /**
     * The size of the GUI window, calculated as the product of the board size
     * and a size factor to easily configure the window size.
     */
    public static final int GUI_SIZE = Board.SIZE * 75;

    private final Game gameBoard;
    private final Menu menu;

    /**
     * Initializes the GUI with the game board and menu.
     * The layout consists of a top index panel, a left index panel, the game
     * board, and a menu at the bottom.
     * The frame is configured with a minimum size and title.
     */
    public GUI() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(Color.white);

        // Initialize the Menu.
        menu = new Menu();
        menu.disableUndo(); // No move is made yet
        this.add(menu, BorderLayout.SOUTH);

        // Initialize the Game.
        gameBoard = new Game();
        this.add(gameBoard, BorderLayout.CENTER);

        // Initialize the index Panels.
        initIndexPanels(c);

        // Set up the main frame.
        this.setMinimumSize(new Dimension(GUI_SIZE, GUI_SIZE));
        this.setTitle("Reversi");
        pack();
    }

    private void initIndexPanels(Container c) {
        int borderSize = 5;
        JPanel indexPanelTop = createIndexPanel(1, Board.SIZE);
        JPanel indexPanelLeft = createIndexPanel(Board.SIZE, 1);
        indexPanelLeft.setBorder(new EmptyBorder(indexPanelTop.getHeight(),
                borderSize, 0, borderSize));
        indexPanelTop.setBorder(new EmptyBorder(0,
                indexPanelLeft.getWidth() + borderSize * 2, 0, 0));
        c.add(indexPanelLeft, BorderLayout.WEST);
        c.add(indexPanelTop, BorderLayout.NORTH);
    }

    private JPanel createIndexPanel(int rows, int cols) {
        JPanel indexPanel = new JPanel();
        indexPanel.setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < Board.SIZE; i++) {
            JLabel label = new JLabel("" + (i + 1));
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            indexPanel.add(label);
        }
        return indexPanel;
    }

    /**
     * Adds a slot (a cell) to the game board at the specified position with
     * the given color.
     *
     * @param position the coordinate of the slot to be added
     * @param color the color of the tile in the slot
     */
    public void addSlot(Coordinate position, Color color) {
        assert position != null;
        gameBoard.addSlot(position, color);
    }

    /**
     * Returns the current difficulty level selected in the menu.
     *
     * @return the current difficulty level
     */
    public int getLevel() {
        return menu.getLevel();
    }

    /**
     * Enables the "Undo" button in the menu, allowing the user to undo the
     * previous move.
     */
    public void enableUndo() {
        menu.enableUndo();
    }

    /**
     * Disables the "Undo" button in the menu, preventing the user from
     * undoing a move.
     */
    public void disableUndo() {
        menu.disableUndo();
    }

    /**
     * Updates the score for the human player in the menu.
     *
     * @param score the new score for the human player
     */
    public void updateScoreHuman(int score) {
        menu.updateScoreHuman(score);
    }

    /**
     * Updates the score for the bot player in the menu.
     *
     * @param score the new score for the bot player
     */
    public void updateScoreBot(int score) {
        menu.updateScoreBot(score);
    }

    /**
     * Updates the color of the slot at the given coordinate on the game board.
     *
     * @param c the coordinate of the slot to update
     * @param color the new color for the slot
     */
    public void updateSlot(Coordinate c, Color color) {
        assert c != null;
        gameBoard.updateSlot(c, color);
    }

    /**
     * Adds a listener to the menu for menu-related events
     *
     * @param actionListener the listener to be added
     */
    public void addMenuListener(ActionListener actionListener) {
        assert actionListener != null;
        menu.addListener(actionListener);
    }

    /**
     * Adds a listener to the game board for move-related events
     *
     * @param mouseListener the listener to be added
     */
    public void addMoveListener(MouseListener mouseListener) {
        assert mouseListener != null;
        gameBoard.addListener(mouseListener);
    }

    /**
     * Displays a message in a dialog box to the user.
     *
     * @param msg the message to be displayed
     */
    public void showMSG(String msg) {
        assert msg != null;
        JOptionPane.showMessageDialog(this, msg, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
