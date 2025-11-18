package reversi.controller;

import reversi.model.Board;
import reversi.model.Coordinate;
import reversi.model.GameBoard;
import reversi.model.enums.Player;
import reversi.model.exceptions.IllegalMoveException;
import reversi.view.GUI;
import reversi.view.components.Menu;
import reversi.view.components.Slot;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Stack;

/**
 * The Controller class manages the interaction between the view (GUI) and
 * the model (game logic) for the Reversi game. It handles user inputs such
 * as mouse clicks and menu actions and updates the game state accordingly.
 */
public class Controller {
    private Thread machineMoveThread = null;
    private final Stack<Board> undoStack = new Stack<>();
    private final GUI view;
    private Board model;

    /**
     * Initializes a new game, creating a GameBoard model and setting up the
     * GUI view. The controller listens for user actions and updates the game
     * state and GUI accordingly.
     *
     * @param model The game board model that represents the game state.
     * @param view The GUI view that displays the game interface and interacts
     *             with the user.
     */
    public Controller(Board model, GUI view) {
        this.model = model;
        this.view = view;

        // Set up the view
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                view.addSlot(new Coordinate(i, j),
                        model.getSlot(i, j).getColor());
            }
        }
        updateScoreValues();
        view.addWindowListener(new WindowListener());
        view.addMoveListener(new MoveListener());
        view.addMenuListener(new MenuListener());
        view.setSize(GUI.GUI_SIZE, GUI.GUI_SIZE);
        view.setVisible(true);
        view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private final class WindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            interruptRunningThread();
        }
    }

    private final class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // New button is pressed.
            if (e.getActionCommand().equals(Menu.NEW_CMD)) {
                interruptRunningThread();
                undoStack.clear();
                cmdNew();
                updateUI();
            }
            // Switch button is pressed.
            if (e.getActionCommand().equals(Menu.SWITCH_CMD)) {
                interruptRunningThread();
                undoStack.clear();
                cmdSwitch();
                updateUI();
            }
            // Undo button is pressed.
            if (e.getActionCommand().equals(Menu.UNDO_CMD)) {
                interruptRunningThread();
                model = undoStack.pop();
                model.setLevel(view.getLevel());
                updateUI();
            }
            // Level combobox is used.
            if (e.getActionCommand().equals(Menu.LEVEL_CMD)) {
                model.setLevel(view.getLevel());
            }
            // Quit button is pressed.
            if (e.getActionCommand().equals(Menu.QUIT_CMD)) {
                view.dispatchEvent(new WindowEvent(view,
                        WindowEvent.WINDOW_CLOSING));
            }
        }

        private void cmdSwitch() {
            if (model.getFirstPlayer() == Player.HUMAN) {
                // The bot is now the first player and immediately makes a move.
                model = new GameBoard(Player.BOT, null);
                startMachineMove();
            } else {
                model = new GameBoard(Player.HUMAN, null);
            }
        }

        private void cmdNew() {
            // If it is not the first game, the first player and level from
            // the last played game will be used.
            model = new GameBoard(model.getFirstPlayer(), null);
            // If the bot is the first player he immediately makes the first
            // move.
            if (model.getFirstPlayer() == Player.BOT) {
                startMachineMove();
            }
        }
    }

    private final class MoveListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // While the machine is calculating its next move, the human
            // player is not allowed to place a new tile.
            if (machineMoveThread != null && machineMoveThread.isAlive()) {
                return;
            }
            Coordinate position = ((Slot) e.getSource()).getPosition();
            undoStack.push(model);
            placeTile(position.getRow(), position.getCol());
        }

        private void placeTile(int row, int col) {
            try {
                model = model.move(row, col);
            } catch (IllegalMoveException | IllegalArgumentException e) {
                view.showMSG(e.getMessage());
                model = undoStack.pop();
                return;
            }
            updateUI();
            // If the game is not over, and it is the bots turn now, the bot
            // makes the next move. Otherwise, the program waits for the next
            // player input.
            if (!gameOver()) {
                if (model.next() == Player.BOT) {
                    startMachineMove();
                } else {
                    view.showMSG("Machine must miss a turn");
                }
            }
        }
    }

    private final class MachineMoveTask implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    model = model.machineMove();
                } catch (InterruptedException ex) {
                    break;
                }
                SwingUtilities.invokeLater(Controller.this::updateUI);
                if (gameOver()) {
                    return;
                }
            } while (humanMissTurn());
        }
    }

    private void updateUI() {
        updateGameBoard();
        updateUndoButton();
        updateScoreValues();
    }

    private void updateGameBoard() {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                view.updateSlot(new Coordinate(i, j),
                        model.getSlot(i, j).getColor());
            }
        }
    }

    private void updateUndoButton() {
        if (undoStack.isEmpty()) {
            view.disableUndo();
        } else {
            view.enableUndo();
        }
    }

    private void updateScoreValues() {
        view.updateScoreHuman(model.getNumberOfHumanTiles());
        view.updateScoreBot(model.getNumberOfMachineTiles());
    }

    private void startMachineMove() {
        machineMoveThread = new Thread(new MachineMoveTask());
        machineMoveThread.start();
    }

    private void interruptRunningThread() {
        if (machineMoveThread != null && machineMoveThread.isAlive()) {
            machineMoveThread.interrupt();
        }
    }

    private boolean humanMissTurn() {
        assert model != null;
        if (model.next() != Player.HUMAN) {
            view.showMSG("You must miss a turn.");
            return true;
        }
        return false;
    }

    private boolean gameOver() {
        assert model != null;
        if (model.gameOver()) {
            switch (model.getWinner()) {
                case BOT -> view.showMSG("Sorry! Machine wins.");
                case HUMAN -> view.showMSG("Congratulations! You won.");
                default -> view.showMSG("Nobody wins. Tie.");
            }
            return true;
        } else {
            return false;
        }
    }
}
