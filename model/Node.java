package reversi.model;

/**
 * Represents a node in a game tree for the Reversi game.
 * <p>
 * Each node contains a game board and an array of child nodes representing
 * possible future game states.
 * This class is used to build decision trees for the implementation of a
 * Reversi game AI to evaluate different game states.
 * </p>
 */
public class Node {

    // The game board associated with this node.
    private final GameBoard board;

    // The children nodes representing possible future game states.
    private final Node[] children;

    /**
     * Constructs a new {@code Node} object for the given game board and its
     * possible children.
     *
     * @param board The {@code GameBoard} associated with this node,
     *              representing a particular game state.
     * @param children An array of {@code Node} objects representing the
     *                 possible next game states (children) after the current
     *                 state. Can be {@code null} if no children exist.
     */
    public Node(Board board, Node[] children) {
        this.board = (GameBoard) board;
        this.children = children;
    }

    /**
     * Gets the game board associated with this node.
     *
     * @return The {@code GameBoard} representing the current game state.
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Gets the children nodes of this node, representing possible future
     * game states.
     *
     * @return An array of {@code Node} objects representing the children of
     * this node.
     *         Returns {@code null} if there are no children.
     */
    public Node[] getChildren() {
        return children;
    }
}
