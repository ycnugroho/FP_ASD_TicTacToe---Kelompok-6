package TicTacToe;

/**
 * Abstract superclass for all AI players with different strategies.
 * To construct an AI player:
 * 1. Construct an instance (of its subclass) with the game Board
 * 2. Call setSeed() to set the computer's seed
 * 3. Call move() which returns the next move in an int[2] array of {row, col}.
 *
 * The implementation subclasses need to override abstract method move().
 * They shall not modify Cell[][], i.e., no side effect expected.
 * Assume that next move is available, i.e., not game-over yet.
 */
public abstract class AIPlayer {
    protected int ROWS;  // number of rows
    protected int COLS;  // number of columns
 
    protected Cell[][] cells; // the board's ROWS-by-COLS array of Cells
    protected Seed mySeed;    // computer's seed
    protected Seed oppSeed;   // opponent's seed
 
    /** Constructor with reference to game board */
    public AIPlayer(Board board) {
       this.cells = board.cells;
       this.ROWS = Board.ROWS;
       this.COLS = Board.COLS;
    }
 
    /** Set/change the seed used by computer and opponent */
    public void setSeed(Seed seed) {
       this.mySeed = seed;
       this.oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }
 
    /** Abstract method to get next move. Return int[2] of {row, col} */
    public abstract int[] move();
 }
