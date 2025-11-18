package reversi.model.enums;

import java.awt.Color;

/**
 * Enum representing the players in a Reversi game.
 * <p>
 * This enum defines three types of players: the human player, the bot
 * (computer) player, and the empty spots on the game board.
 * Each player type has a string representation and a corresponding color for
 * display.
 * </p>
 */
public enum Player {

    /**
     * Represents the human player, typically marked with an "X".
     * The human player is colored blue.
     */
    HUMAN("X", Color.BLUE),

    /**
     * Represents the bot (computer) player, typically marked with an "O".
     * The bot player is colored red.
     */
    BOT("O", Color.RED),

    /**
     * Represents an empty spot on the board, typically marked with a ".".
     * Empty spots do not have a color associated with them.
     */
    EMPTY(".", null);

    // The string representation of the player.
    private final String value;

    // The color associated with the player (or null for empty spots).
    private final Color color;

    /**
     * Constructor to associate a string value and color with each player type.
     *
     * @param value The string representation of the player or an empty spot
     *              on the board (e.g., "X" for human, "O" for bot, "." for
     *              empty).
     * @param color The color associated with the player or null for an empty
     *              spot.
     */
    Player(String value, Color color) {
        this.value = value;
        this.color = color;
    }

    /**
     * Returns the string representation of the player.
     *
     * @return A string representing the player ("X" for human, "O" for bot,
     *         "." for empty).
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the color associated with the player.
     *
     * @return The color associated with the player (e.g., blue for human,
     *         red for bot, or null for empty spots).
     */
    public Color getColor() {
        return color;
    }
}
