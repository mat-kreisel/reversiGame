package reversi.model.exceptions;

/**
 * Exception thrown when an illegal move is attempted in the Reversi game.
 * <p>
 * This exception is thrown when a move is made while the game is over or
 * when it is not the player's turn.
 * </p>
 */
public class IllegalMoveException extends RuntimeException {
    /**
     * Constructs a new {@code IllegalMoveException} with the specified
     * detail message.
     * <p>
     * This exception is thrown when a move is attempted under invalid
     * conditions, such as:
     * <ul>
     *     <li>The game has already ended (i.e., no further moves are
     *     allowed).</li>
     *     <li>The move is made when it is not the player's turn.</li>
     * </ul>
     * </p>
     *
     * @param message The detail message that explains the cause of the
     *                exception.
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
