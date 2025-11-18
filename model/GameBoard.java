package reversi.model;

import reversi.model.enums.Player;
import reversi.model.exceptions.IllegalMoveException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

import static reversi.model.enums.Player.BOT;
import static reversi.model.enums.Player.HUMAN;
import static reversi.model.enums.Player.EMPTY;

/**
 * Represents the game board for the Reversi (Othello) game.
 * Handles board initialization, player moves, and game state management.
 */
public class GameBoard implements Board {
    /**
     * The maximum level of difficulty for the AI opponent.
     * Values range from 1 to 5, with 5 being the hardest.
     */
    public static final int MAX_LEVEL = 5;
    /**
     * The default level of difficulty for the AI opponent.
     */
    public static final int DEFAULT_LEVEL = 3;
    /**
     * A 2D array representing the score value map for different positions on
     * the board.
     * The values are used by the AI to prioritize certain positions based on
     * the game strategy.
     * Higher values indicate more favorable positions.
     */
    private static final int[][] SCORE_VALUE_MAP = {
            {9999, 5, 500, 200, 200, 500, 5, 9999},
            {5, 1, 50, 150, 150, 50, 1, 5},
            {500, 50, 250, 100, 100, 250, 50, 500},
            {200, 150, 100, 50, 50, 100, 150, 200},
            {200, 150, 100, 50, 50, 100, 150, 200},
            {500, 50, 250, 100, 100, 250, 50, 500},
            {5, 1, 50, 150, 150, 50, 1, 5},
            {9999, 5, 500, 200, 200, 500, 5, 9999}
    };
    private static final int ROUNDING_PRECISION = 3;
    private static int level;
    private static Player firstPlayer;
    private int tilesHuman;
    private int tilesBot;
    private Player currentPlayer;
    private Player[][] board;
    private int tileScoreHuman;
    private int tileScoreBot;
    private Coordinate move;
    /**
     * A map that represents the possible moves for the human player in the
     * game. The map's keys are {@link Coordinate} objects representing the
     * coordinates where the human player can place their tile. The values
     * are lists of {@link Coordinate} objects that represent the tiles that
     * will be flipped when the human player places their tile at the key
     * coordinates.
     */
    private Map<Coordinate, List<Coordinate>> possibleMovesHuman;
    /**
     * A map that represents the possible moves for the bot player in the game.
     * The map's keys are {@link Coordinate} objects representing the
     * coordinates where the bot player can place their tile. The values are
     * lists of {@link Coordinate} objects that represent the tiles that will
     * be flipped when the bot places their tile at the key coordinates.
     */
    private Map<Coordinate, List<Coordinate>> possibleMovesBot;
    /**
     * A map that represents the available (empty) spaces around the human
     * player's placed tiles. The map's keys are {@link Coordinate} objects
     * representing the coordinates of the human player's placed tiles. The
     * values are lists of {@link Coordinate} objects representing the empty
     * spaces (i.e., spaces with the value {@code EMPTY} on the board)
     * surrounding each human's placed tile.
     */
     private Map<Coordinate, List<Coordinate>> freeSpacesHuman;
    /**
     * A map that represents the available (empty) spaces around the bot
     * player's placed tiles. The map's keys are {@link Coordinate} objects
     * representing the coordinates of the bot player's placed tiles. The
     * values are lists of {@link Coordinate} objects representing the empty
     * spaces (i.e., spaces with the value {@code EMPTY} on the board)
     * surrounding each bot's placed tile.
     */
    private Map<Coordinate, List<Coordinate>> freeSpacesBot;
    private double scoreBoard;

    /**
     * Constructs a new game board with the specified first player and
     * difficulty level.
     * This constructor initializes the game board, sets up the difficulty
     * level and prepares maps for storing possible moves and free spaces for
     * both players.
     *
     * @param firstPlayer the player who starts the game (either HUMAN or BOT)
     * @param level  the difficulty level for the AI. This parameter must be
     *               null if the level should be the same value as the level
     *               from the last played game.
     */
    public GameBoard(Player firstPlayer, Integer level) {
        if (level != null) {
            GameBoard.level = level;
        }
        GameBoard.firstPlayer = firstPlayer;
        currentPlayer = null;
        possibleMovesHuman = new HashMap<>();
        possibleMovesBot = new HashMap<>();
        freeSpacesHuman = new HashMap<>();
        freeSpacesBot = new HashMap<>();
        initializeBoard();
    }

    /**
     * This Constructor is used to make a clone of the current game state and
     * thus only used privately in this class.
     * This constructor copies the current board configuration, possible
     * moves, and free spaces for both players.
     */
    private GameBoard(Player[][] board,
                     Map<Coordinate, List<Coordinate>> mapPossibleMovesHuman,
                     Map<Coordinate, List<Coordinate>> possibleMovesBot,
                     Map<Coordinate, List<Coordinate>> mapFreeSpacesHuman,
                     Map<Coordinate, List<Coordinate>> mapFreeSpacesBot) {
        this.board = board;
        this.possibleMovesHuman = mapPossibleMovesHuman;
        this.possibleMovesBot = possibleMovesBot;
        this.freeSpacesHuman = mapFreeSpacesHuman;
        this.freeSpacesBot = mapFreeSpacesBot;
    }

    private void initializeBoard() {
        board = new Player[SIZE][SIZE];
        // Initialize every field EMPTY.
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], EMPTY);
        }
        switch (firstPlayer) {
            case HUMAN -> initializeStartingPositions(HUMAN, BOT);
            case BOT -> initializeStartingPositions(BOT, HUMAN);
            default -> throw new IllegalStateException("Unexpected value: "
                    + firstPlayer);
        }
    }

    /**
     * Initialize starting positions based on the first player.
     */
    private void initializeStartingPositions(Player firstPlayer,
                                             Player secondPlayer) {
        int tilePivot = Board.SIZE / 2;
        addTile(tilePivot - 1, tilePivot, firstPlayer);
        addTile(tilePivot, tilePivot - 1, firstPlayer);
        addTile(tilePivot - 1, tilePivot - 1, secondPlayer);
        addTile(tilePivot, tilePivot, secondPlayer);
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE;
    }

    private boolean isFree(int row, int col) {
        return board[row][col] == EMPTY;
    }

    private boolean isAvailable(int row, int col) {
        return isInBounds(row, col) && isFree(row, col);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Player next() {
        Player next;
        // In the first turn the current player is not set to a
        // value, because no player made a move on this board.
        // The next player on the very first board is always the
        // first player.
        if (currentPlayer == null) {
            if (firstPlayer == HUMAN) {
                next = HUMAN;
            } else {
                next = BOT;
            }
            return next;
        }
        // Switch the players.
        if (currentPlayer == HUMAN) {
            next = BOT;
            // If no move is possible for the bot, then its turn is skipped.
            if (possibleMovesBot.isEmpty()) {
                next = HUMAN;
            }
        } else {
            next = HUMAN;
            // If no move is possible for the human, then its turn is skipped.
            if (possibleMovesHuman.isEmpty()) {
                next = BOT;
            }
        }
        return next;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Board move(int row, int col) throws IllegalMoveException,
            IllegalArgumentException {
        if (gameOver() || next() != HUMAN) {
            String message;
            if (gameOver()) {
                message = "Game is already over.";
            } else {
                message = "It is not the humans turn.";
            }
            throw new IllegalMoveException(message);
        }
        if (!isAvailable(row, col)
                || !possibleMovesHuman.containsKey(new Coordinate(row, col))) {
            throw new IllegalArgumentException("Invalid move at (" + (row + 1)
                    + ", " + (col + 1) + ")");
        }
        GameBoard cloned = (GameBoard) this.clone();
        cloned.addTile(row, col, cloned.currentPlayer);
        return cloned;
    }

    private void addTile(int row, int col, Player player) {
        board[row][col] = player;
        winTile(row, col, player);
        List<Coordinate> tilesToFlip = player == HUMAN
                ? possibleMovesHuman.get(new Coordinate(row, col))
                : possibleMovesBot.get(new Coordinate(row, col));
        if (tilesToFlip != null) {
            for (Coordinate tile : tilesToFlip) {
                flipTile(tile.getRow(), tile.getCol(), player);
            }
        }
        updateFreeSpacesAdd(row, col, player);
        computePossibleMoves();
        computeScoreBoard();
    }

    private void flipTile(int row, int col, Player player) {
        Player prevPlayer = board[row][col];
        loseTile(row, col, prevPlayer);
        board[row][col] = player;
        winTile(row, col, player);
        updateFreeSpacesFlip(row, col, player);
    }

    private void computeScoreBoard() {
        int potentialScoreBot = 0;
        for (List<Coordinate> freeSpaces : freeSpacesHuman.values()) {
            potentialScoreBot += freeSpaces.size();
        }
        int potentialScoreHuman = 0;
        for (List<Coordinate> freeSpaces : freeSpacesBot.values()) {
            potentialScoreHuman += freeSpaces.size();
        }
        int mobilityScoreBot = possibleMovesBot.size();
        int mobilityScoreHuman = possibleMovesHuman.size();
        double relativeOccupiedTilesM =
                ((double) 64 / (tilesHuman + tilesBot));
        double relativeOccupiedTilesP =
                ((double) 64 / ((tilesHuman + tilesBot) * 2));
        double scoreT = tileScoreBot - (1.5 * tileScoreHuman);
        double scoreM = relativeOccupiedTilesM
                * ((3.0 * mobilityScoreBot) - (4.0 * mobilityScoreHuman));
        double scoreP = relativeOccupiedTilesP
                * ((2.5 * potentialScoreBot) - (3.0 * potentialScoreHuman));
        scoreBoard = scoreT + scoreM + scoreP;
        // Round the result of the score calculation to three digits.
        scoreBoard = BigDecimal.valueOf(scoreBoard)
                .setScale(ROUNDING_PRECISION, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void winTile(int row, int col, Player player) {
        if (player == HUMAN) {
            tileScoreHuman += SCORE_VALUE_MAP[row][col];
            tilesHuman++;
        } else {
            tileScoreBot += SCORE_VALUE_MAP[row][col];
            tilesBot++;
        }
    }

    private void loseTile(int row, int col, Player player) {
        if (player == HUMAN) {
            tileScoreHuman -= SCORE_VALUE_MAP[row][col];
            tilesHuman--;
        } else {
            tileScoreBot -= SCORE_VALUE_MAP[row][col];
            tilesBot--;
        }
    }

    private void computePossibleMoves() {
        possibleMovesHuman.clear();
        possibleMovesBot.clear();
        possibleMoves(freeSpacesBot, HUMAN);
        possibleMoves(freeSpacesHuman, BOT);
    }

    private void possibleMoves(Map<Coordinate, List<Coordinate>> freeSpaces,
                               Player player) {
        for (Map.Entry<Coordinate, List<Coordinate>> entry
                : freeSpaces.entrySet()) {
            Coordinate key = entry.getKey();
            List<Coordinate> values = entry.getValue();
            // Check for every free space if it is also a valid possible move.
            for (Coordinate value : values) {
                checkPossibleMove(key, value, player);
            }
        }
    }

    private void checkPossibleMove(Coordinate key, Coordinate value,
                                   Player player) {
        if (!isInBounds(value.getRow(), value.getCol())) {
            return;
        }
        Coordinate direction =
                new Coordinate(key.getRow() - value.getRow(),
                        key.getCol() - value.getCol());
        if (player == HUMAN) {
            possibleMove(HUMAN, BOT, key, direction, value);
        } else {
            possibleMove(BOT, HUMAN, key, direction, value);
        }
    }

    private void possibleMove(Player player, Player enemy,
                              Coordinate freeSpace, Coordinate direction,
                              Coordinate possibleMove) {
        Map<Coordinate, List<Coordinate>> possibleMoves;
        if (player == BOT) {
            possibleMoves = possibleMovesBot;
        } else {
            possibleMoves = possibleMovesHuman;
        }
        List<Coordinate> tilesToFlip = new ArrayList<>();
        int fRow = freeSpace.getRow();
        int fCol = freeSpace.getCol();
        // As long as the tiles in the specified direction are of the other
        // player (i.e. enemy) and in the bounds of the game size, add them to
        // the list of tiles to flip for this possible move.
        do {
            tilesToFlip.add(new Coordinate(fRow, fCol));
            fRow += direction.getRow();
            fCol += direction.getCol();
        } while (isInBounds(fRow, fCol) && board[fRow][fCol] == enemy);
        if (isInBounds(fRow, fCol) && board[fRow][fCol] == player) {
            // If this possible move already exists, append the tiles to
            // flip to this possible move's list values, otherwise create a
            // new entry in the map.
            if (possibleMoves.containsKey(possibleMove)) {
                List<Coordinate> values = possibleMoves.get(possibleMove);
                values.addAll(tilesToFlip);
            } else {
                possibleMoves.put(possibleMove, tilesToFlip);
            }
        }
    }

    /**
     * Updates the maps that hold the free spaces values whenever a tile is
     * flipped.
     */
    private void updateFreeSpacesFlip(int row, int col, Player player) {
        Coordinate key = new Coordinate(row, col);
        List<Coordinate> freeSpacesAroundKey = new ArrayList<>();
        if (player == HUMAN) {
            freeSpacesAroundKey.addAll(freeSpacesBot.get(key));
            freeSpacesBot.remove(key);
            freeSpacesHuman.put(key, freeSpacesAroundKey);
        } else {
            freeSpacesAroundKey.addAll(freeSpacesHuman.get(key));
            freeSpacesHuman.remove(key);
            freeSpacesBot.put(key, freeSpacesAroundKey);
        }
    }

    /**
     * Updates the maps that hold the free spaces values whenever a new tile
     * is added.
     */
    private void updateFreeSpacesAdd(int row, int col, Player player) {
        Coordinate key = new Coordinate(row, col);
        // Remove the inserted key from the values of the free spaces maps.
        removeFromAllMaps(key);
        // Check for every tile around the newly inserted if it is free. If
        // that is the case, the add it to its map as a new value.
        int currentRow;
        int currentCol;
        int[] directions = {-1, 0, 1};
        List<Coordinate> freeSpacesAroundKey = new ArrayList<>();
        for (int dRow : directions) {
            for (int dCol : directions) {
                // Skip the (0,0) direction to avoid checking the center point.
                if ((dRow == 0 && dCol == 0)) {
                    continue;
                }
                currentRow = row + dRow;
                currentCol = col + dCol;
                if (isAvailable(currentRow, currentCol)) {
                    Coordinate value = new Coordinate(currentRow, currentCol);
                    freeSpacesAroundKey.add(value);
                }
            }
        }
        if (player == HUMAN) {
            freeSpacesHuman.put(key, freeSpacesAroundKey);
        } else {
            freeSpacesBot.put(key, freeSpacesAroundKey);
        }
    }

    private void removeFromAllMaps(Coordinate key) {
        for (Map.Entry<Coordinate, List<Coordinate>> entry
                : freeSpacesHuman.entrySet()) {
            List<Coordinate> freeSpaces = entry.getValue();
            freeSpaces.remove(key);
        }
        for (Map.Entry<Coordinate, List<Coordinate>> entry
                : freeSpacesBot.entrySet()) {
            List<Coordinate> freeSpaces = entry.getValue();
            freeSpaces.remove(key);
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Board machineMove() throws InterruptedException {
        Node root = new Node(this, generateChildren(0, this));
        generateScores(root);
        Node bestChild = getBestChild(root);
        assert bestChild != null;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return bestChild.getBoard();
    }

    /**
     * This move method is only called internally to process the machine
     * move.
     */
    private GameBoard move(Coordinate coordinate) {
        assert coordinate != null;
        GameBoard cloned = (GameBoard) this.clone();
        cloned.addTile(coordinate.getRow(), coordinate.getCol(),
                cloned.currentPlayer);
        cloned.move = coordinate;
        return cloned;
    }

    private Node[] generateChildren(int depth, GameBoard board)
            throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (depth >= level || gameOver()) {
            return null;
        }
        Set<Coordinate> possibleMoves;
        switch (board.next()) {
            case BOT -> possibleMoves = board.possibleMovesBot.keySet();
            case HUMAN -> possibleMoves = board.possibleMovesHuman.keySet();
            default -> throw new IllegalStateException("Unexpected value: "
                    + board);
        }
        if (possibleMoves.isEmpty()) {
            return null;
        } else {
            Node[] children = new Node[possibleMoves.size()];
            int counter = 0;
            for (Coordinate possibleMove : possibleMoves) {
                GameBoard gameBoard = board.move(possibleMove);
                children[counter] = new Node(gameBoard,
                        generateChildren(depth + 1, gameBoard));
                counter++;
            }
            return children;
        }
    }

    /**
     * This Method calculates the child of {@code n} with the highest score
     * value. Returns {@code n} if the Node {@code n} has no children.
     */
    private Node getBestChild(Node n) {
        assert n != null;
        Node[] children = n.getChildren();
        if (children == null) {
            return n;
        }
        Node max = children[0];
        for (Node child : children) {
            double maxScore = max.getBoard().scoreBoard;
            double childScore = child.getBoard().scoreBoard;
            if (childScore == maxScore) {
                max = compareNodes(child, max);
            } else if (childScore > maxScore) {
                max = child;
            }
        }
        return max;
    }

    /**
     * This Method calculates the child of {@code n} with the lowest score
     * value. Returns {@code n} if the Node {@code n} has no children.
     */
    private Node getWorstChild(Node n) {
        assert n != null;
        Node[] children = n.getChildren();
        if (children == null) {
            return n;
        }
        Node min = children[0];
        for (Node child : children) {
            double minScore = min.getBoard().scoreBoard;
            double childScore = child.getBoard().scoreBoard;
            if (childScore == minScore) {
                min = compareNodes(child, min);
            }
            if (childScore < minScore) {
                min = child;
            }
        }
        return min;
    }

    /**
     * If two nodes have the same score value, this method is called in order
     * to compare the two nodes for their row and col values.
     * @return Returns the node with the lower row value. If both nodes are in
     *         the same row, the node with the lower col value is returned.
     */
    private Node compareNodes(Node n1, Node n2) {
        Coordinate moveN1 = n1.getBoard().move;
        Coordinate moveN2 = n2.getBoard().move;
        if (moveN1.getRow() != moveN2.getRow()) {
            if (moveN1.getRow() < moveN2.getRow()) {
                return n1;
            } else {
                return n2;
            }
        } else {
            if (moveN1.getCol() < moveN2.getCol()) {
                return n1;
            } else {
                return n2;
            }
        }
    }

    private void generateScores(Node n)  throws InterruptedException {
        if (Thread.interrupted()) {
            System.out.println("t interrupted");
            throw new InterruptedException();
        }
        GameBoard board = n.getBoard();
        Node[] children = n.getChildren();
        if (children != null) {
            for (Node child : children) {
                generateScores(child);
            }
            // Get the maximum of the children (the best possible move) if the
            // bot placed the tile and the minimum (worst possible move) if
            // the human placed the tile.
            if (board.next() == BOT) {
                Node bestChild = getBestChild(n);
                board.scoreBoard += bestChild.getBoard().scoreBoard;
            } else {
                Node worstChild = getWorstChild(n);
                board.scoreBoard += worstChild.getBoard().scoreBoard;
            }
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void setLevel(int level) {
        GameBoard.level = level;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public boolean gameOver() {
        return possibleMovesBot.isEmpty() && possibleMovesHuman.isEmpty();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Player getWinner() {
        Player winner;
        if (tilesHuman > tilesBot) {
            winner = HUMAN;
        } else if (tilesBot > tilesHuman) {
            winner = BOT;
        } else {
            winner = EMPTY;
        }
        return winner;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public int getNumberOfHumanTiles() {
        return tilesHuman;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public int getNumberOfMachineTiles() {
        return tilesBot;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Player getSlot(int row, int col) {
        return board[row][col];
    }

    /**
     * This Method creates a clone of the current GameBoard Object making a
     * deep copy of the fields and properties. The only exception is the field
     * {@code currentPlayer} which is set to the return value of the
     * {@link GameBoard#next()} Method. This is because the current player of
     * the cloned board is always the next player of this board.
     *
     * @return A clone.
     */
    @Override
    public Board clone() {
        Player[][] copyBoard = new Player[SIZE][SIZE];
        // Copy the references to Player objects into the new array.
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copyBoard[i], 0, board[i].length);
        }
        // Clone the Maps.
        Map<Coordinate, List<Coordinate>> clonedMapPossibleMovesHuman =
                cloneMap(possibleMovesHuman);
        Map<Coordinate, List<Coordinate>> clonedMapPossibleMovesBot =
                cloneMap(possibleMovesBot);
        Map<Coordinate, List<Coordinate>> clonedMapFreeSpacesHuman =
                cloneMap(freeSpacesHuman);
        Map<Coordinate, List<Coordinate>> clonedMapFreeSpacesBot =
                cloneMap(freeSpacesBot);
        GameBoard clonedBoard = new GameBoard(copyBoard,
                clonedMapPossibleMovesHuman, clonedMapPossibleMovesBot,
                clonedMapFreeSpacesHuman, clonedMapFreeSpacesBot);
        // Clone the other fields.
        clonedBoard.tilesHuman = this.tilesHuman;
        clonedBoard.tilesBot = this.tilesBot;
        clonedBoard.tileScoreHuman = this.tileScoreHuman;
        clonedBoard.tileScoreBot = this.tileScoreBot;
        clonedBoard.scoreBoard = this.scoreBoard;
        // The current player of the cloned board is the next player of this
        // board.
        clonedBoard.currentPlayer = this.next();
        return clonedBoard;
    }

    private Map<Coordinate, List<Coordinate>> cloneMap(Map<Coordinate,
            List<Coordinate>> originalMap) {
        Map<Coordinate, List<Coordinate>> clonedMap = new HashMap<>();
        for (Map.Entry<Coordinate, List<Coordinate>> entry
                : originalMap.entrySet()) {
            clonedMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return clonedMap;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                output.append(board[i][j]);
                if (j < board[0].length - 1) {
                    output.append(" ");
                }
            }
            output.append("\n");
        }
        return output.toString();
    }
}
