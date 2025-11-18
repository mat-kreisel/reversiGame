package reversi.view.components;

import reversi.model.Coordinate;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseListener;

/**
 * The {@code Slot} class represents a single slot on the Reversi game board.
 * Each slot can hold a color, which indicates the presence of a player's
 * piece (human or bot).
 * The slot is rendered as a circle within the panel, and it responds to
 * mouse clicks via a {@link MouseListener}.
 */
public class Slot extends JPanel {
    private Color color;
    private final Coordinate position;

    /**
     * Constructs a new {@code Slot} with the specified color and position.
     *
     * @param color the color of the piece in the slot (could be null for an
     *              empty slot)
     * @param position the position of the slot on the game board
     */
    public Slot(Color color, Coordinate position) {
        this.color = color;
        this.setBackground(Color.GREEN);
        this.position = position;
    }

    /**
     * Returns the position of this slot on the game board.
     *
     * @return the {@link Coordinate} representing the position of this slot
     */
    public Coordinate getPosition() {
        return position;
    }

    /**
     * Updates the color of the slot and repaints it.
     * This method is called when the color of the slot changes (e.g., when a
     * new piece is placed).
     *
     * @param color the new color to be applied to the slot
     */
    public void updateColor(Color color) {
        this.color = color;
        repaint();
    }

    /**
     * Paints the slot as a circle with the current color.
     * This method is invoked when the slot needs to be rendered.
     *
     * @param g the {@link Graphics} object used to paint the slot
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (color != null) {
            int offset = 5;
            int relativeSizeToParentFactor = -10;
            g.setColor(color);
            g.fillOval(offset, offset, getWidth() + relativeSizeToParentFactor,
                    getHeight() + relativeSizeToParentFactor);
        }
    }

    /**
     * Adds a mouse listener to this slot, enabling it to respond to mouse
     * clicks.
     *
     * @param mouseListener the {@link MouseListener} to be added to this slot
     */
    public void addListener(MouseListener mouseListener) {
        this.addMouseListener(mouseListener);
    }
}
