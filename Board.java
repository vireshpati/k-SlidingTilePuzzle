import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Board {

    // An IMMUTABLE class that represents the state of the
    // board for an n x m sliding tile puzzle.  There are
    // two ways to indicate the position of a tile on the
    // board: first it can be a (row, column) pair; second
    // it can be an index into an n x m vector.  We will
    // use a one dimensional vector for state of the board,
    // but it will be convenient to have functions that
    // go back and forth between the two representations
    // for the position of a tile.

    // Assertions
    //
    //     I strongly urge you to use assertions to validate
    //     the parameters passed to the methods in this class
    //     and have indicated how to do so for the first few
    //     methods.  You should enable assertions when running
    //     your code during development (the -ea option to java):
    //
    //         java -ea Puzzle -rows 4 -columns 4 ...
    //
    //     and AssertionError with be thrown whenever an assert
    //     statement fails.  When running your program as a
    //     finished product, omit the -ea option and assertions
    //     will not be checked so your program will run faster.

    private final int rows;
    private final int columns;
    private final int[] board;

    public Board(int rows, int columns) {
        // Construct a board in the solved configuration
        this.rows = rows;
        this.columns = columns;
        board = goal(rows, columns);
        validate(rows, columns, this.board);
    }

    public Board(int rows, int columns, int[] board) {
        // Construct a board with a given configuration

        this.rows = rows;
        this.columns = columns;
        this.board = Arrays.copyOf(board, rows * columns);
        validate(rows, columns, this.board);
    }

    public Board(Board other, Direction direction) {
        // Construct a board by moving the empty square
        // in a specified position from a given board
        // configuration

        this.rows = other.rows;
        this.columns = other.columns;
        this.board = Arrays.copyOf(other.move(direction), rows * columns);
        validate(rows, columns, this.board);

    }

    public static int[] goal(int rows, int columns) {
        // All tiles are in ascending order
        // Empty square is in lower right corner

        int[] goal = new int[rows * columns];
        for (int i = 1; i <= goal.length; i++) goal[i - 1] = i % goal.length;
        return goal;
    }

    public int rows() {
        // Number of rows for this board
        return rows;
    }

    public int columns() {
        // Number of columns for this board
        return columns;
    }

    public int empty() {
        // Position of the empty square
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }

    public int get(int index) {
        // Contents of a particular position on the board
        assert isValidIndex(index);
        return board[index];
    }

    public int get(int row, int column) {
        // Contents of a particular position on the board
        assert isValidRow(row);
        assert isValidColumn(column);
        return board[index(row, column)];
    }

    public int index(int row, int column) {
        // Conversion from row/column to position
        return row * columns + column;
    }

    public int row(int index) {
        // Conversion from index to row
        return index / columns;
    }

    public int column(int index) {
        // Conversion from index to column
        return index % columns;
    }

    public int position(int position, Direction direction) {
        // Position of the adjacent square in a given direction

        switch (direction) {

            case UP -> {
                if (isValidIndex(position - columns)) return position - columns;
            }
            case DOWN -> {
                if (isValidIndex(position + columns)) return position + columns;
            }
            case LEFT -> {
                if (isValidIndex(position - 1) && position % columns != 0) return position - 1;
            }
            case RIGHT -> {
                if (isValidIndex(position + 1) && (position + 1) % columns != 0) return position + 1;
            }

        }
        return -1;

    }

    public Direction[] moves(int position) {
        // The list of possible moves from a given board position
        int count = 0;
        for (Direction d : Direction.values()) {
            if (position(position, d) >= 0) count++;
        }

        Direction[] moves = new Direction[count];
        int i = 0;
        for (Direction d : Direction.values()) {
            if (position(position, d) >= 0) {
                moves[i] = d;
                i++;
            }
        }
        return moves;
    }

    private int[] move(Direction direction) {
        // Creates a new board array with the empty
        // square moved in the indicated direction

        int[] newBoard = Arrays.copyOf(board, board.length);

        newBoard[empty()] = board[position(empty(), direction)];
        newBoard[position(empty(), direction)] = 0;

        return newBoard;

    }

    public boolean equals(Board other) {
        return Arrays.equals(board, other.board);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Board && this.equals((Board) other);
    }

    @Override
    public int hashCode() {
        // You WILL want a better hashCode function
        // hashCode and equals MUST be compatible
        int radix = rows * columns;
        int hash = board[0];
        for (int i = 1; i < board.length; i++) {
            hash *= radix;
            hash += board[i];
        }
        return hash;
    }

    @Override
    public String toString() {
        String result = "";
        String separator = "";
        for (int i : board) {
            result += separator;
            result += String.format("%2d", i);
            separator = " ";
        }
        return result;
    }

    public void print() {
        for (int row = 0; row < this.rows(); row++) {
            String line = "";
            if (row > 0) printRowSeparator();
            String separator = "";
            for (int col = 0; col < this.columns(); col++) {
                int tile = this.get(row, col);
                line += separator;
                if (tile > 0) {
                    line += String.format(" %2d ", this.get(row, col));
                } else {
                    line += "    ";
                }
                separator = "|";
            }
            System.out.println(line);
        }
    }

    private void printRowSeparator() {
        String line = "";
        String separator = "";
        for (int col = 0; col < this.columns(); col++) {
            line += separator;
            line += "----";
            separator = "+";
        }
        System.out.println(line);
    }

    public boolean isGoal() {
        return Arrays.equals(board, goal(rows, columns));
    }

    private boolean isValidRow(int row) {
        return row >= 0 && row < rows;
    }

    public boolean isValidColumn(int column) {
        return column >= 0 && column < columns;
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index < rows * columns;
    }

    private void validate(int rows, int columns, int[] board) {
//         Check that size = rows * columns;
//         Check that each tile is valid:
//            Tile values are in range 0 ... size-1
//            There are no duplicate tiles

        if (board.length != rows * columns) throw new IllegalArgumentException("Invalid size");
        Set<Integer> tiles = new HashSet<>();
        for (int i : board)
            if (!tiles.add(i) || i < 0 || i >= board.length) throw new IllegalArgumentException("Invalid board");


    }

    public int len() {
        return board.length;
    }


}
