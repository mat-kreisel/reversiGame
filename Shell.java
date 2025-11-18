package reversi;

import reversi.controller.Controller;
import reversi.model.Board;
import reversi.model.GameBoard;
import reversi.model.enums.Player;
import reversi.view.GUI;

/**
 * A utility class that serves as the entry point for the Reversi Game.
 *
 * Provides the main method to initiate the application.
 */
public final class Shell {
    /**
     * Utility class constructor preventing instantiation.
     */
    private Shell() { }

    /**
     * The entry point of the application.
     *
     * @param args Command-line arguments passed to the application.
     *             (Not used in this implementation.)
     */
    public static void main(String[] args) {
        // Initialize the Model.
        Board model = new GameBoard(Player.HUMAN, GameBoard.DEFAULT_LEVEL);

        // Initialize the View.
        GUI view = new GUI();

        // Initialize the Controller.
        Controller c = new Controller(model, view);
    }
}
