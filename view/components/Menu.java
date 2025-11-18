package reversi.view.components;

import reversi.model.GameBoard;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * The {@code Menu} class represents the menu panel in the Reversi game's
 * user interface.
 * It includes buttons for starting a new game, quitting, switching players,
 * undoing a move, and a combo box for selecting the difficulty level. It
 * also displays the scores of the human and bot players. This class provides
 * methods for enabling/disabling buttons and updating the game state.
 */
public class Menu extends JPanel {
    /**
     * Command string for starting a new game.
     * Used in menu or button actions to trigger the creation of a new game
     * session.
     */
    public static final String NEW_CMD = "New";

    /**
     * Command string for quitting the game.
     * Used in menu or button actions to exit or close the game.
     */
    public static final String QUIT_CMD = "Quit";

    /**
     * Command string for switching between different game states or modes.
     * Can be used for toggling between views or game modes during gameplay.
     */
    public static final String SWITCH_CMD = "Switch";

    /**
     * Command string for undoing the previous move.
     * Used in menu or button actions to revert the last action made in the
     * game.
     */
    public static final String UNDO_CMD = "Undo";

    /**
     * Command string for changing the game difficulty level.
     * Used in menu or button actions to adjust the game's difficulty settings.
     */
    public static final String LEVEL_CMD = "Level";

    private static final Font SCORE_FONT = new Font("Serif", Font.BOLD, 30);
    private static final int DEFAULT_LEVEL_INDEX = 2;
    // UI components
    private final JLabel scoreHuman = new JLabel();
    private final JLabel scoreBot = new JLabel();
    private final JButton newB = new JButton(NEW_CMD);
    private final JButton quitB = new JButton(QUIT_CMD);
    private final JButton undoB = new JButton(UNDO_CMD);
    private final JButton switchB = new JButton(SWITCH_CMD);
    private final JComboBox<Integer> level;

    /**
     * Constructs a new {@code Menu} panel with buttons for New, Quit,
     * Switch, Undo, and a combo box for selecting the game difficulty level.
     * It also initializes the score labels for both human and bot players.
     */
    public Menu() {
        this.setLayout(new FlowLayout());
        initLabel(scoreHuman, Color.BLUE);
        initButton(newB, NEW_CMD, KeyEvent.VK_N);
        initButton(switchB, SWITCH_CMD, KeyEvent.VK_S);
        initButton(undoB, UNDO_CMD, KeyEvent.VK_U);
        initButton(quitB, QUIT_CMD, KeyEvent.VK_Q);

        // Set up the level combo box
        Integer[] levelRange = new Integer[GameBoard.MAX_LEVEL];
        for (int i = 0; i < GameBoard.MAX_LEVEL; i++) {
            levelRange[i] = i + 1;
        }
        level = new JComboBox<>(levelRange);
        level.setActionCommand(LEVEL_CMD);
        level.setSelectedIndex(DEFAULT_LEVEL_INDEX);
        this.add(level);

        initLabel(scoreBot, Color.RED);
    }

    private void initButton(JButton button, String command, int mnemonic) {
        button.setActionCommand(command);
        button.setMnemonic(mnemonic);
        this.add(button);
    }

    private void initLabel(JLabel label, Color color) {
        label.setFont(SCORE_FONT);
        label.setForeground(color);
        this.add(label);
    }

    /**
     * Enables the "Undo" button, allowing the player to undo their last move.
     */
    public void enableUndo() {
        undoB.setEnabled(true);
    }

    /**
     * Disables the "Undo" button, preventing the player from undoing their
     * move.
     */
    public void disableUndo() {
        undoB.setEnabled(false);
    }

    /**
     * Retrieves the currently selected difficulty level from the combo box.
     *
     * @return the selected difficulty level (an integer between 1 and
     * {@link GameBoard#MAX_LEVEL})
     */
    public int getLevel() {
        assert level.getSelectedItem() != null;
        return (int) level.getSelectedItem();
    }

    /**
     * Updates the score display for the human player.
     *
     * @param score the current score of the human player
     */
    public void updateScoreHuman(int score) {
        scoreHuman.setText("" + score);
    }

    /**
     * Updates the score display for the bot player.
     *
     * @param score the current score of the bot player
     */
    public void updateScoreBot(int score) {
        scoreBot.setText("" + score);
    }

    /**
     * Adds an {@link ActionListener} to the buttons and combo box in the menu.
     * This listener will handle user interactions like clicking buttons or
     * selecting a level.
     *
     * @param actionListener the action listener to be added to the menu
     *                       components
     */
    public void addListener(ActionListener actionListener) {
        newB.addActionListener(actionListener);
        switchB.addActionListener(actionListener);
        undoB.addActionListener(actionListener);
        quitB.addActionListener(actionListener);
        level.addActionListener(actionListener);
    }
}
