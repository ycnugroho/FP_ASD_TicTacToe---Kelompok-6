package TicTacToe;

/**
 * AI player with rule-based strategy.
 */
public class AIPlayerRuleBased extends AIPlayer {

    /** Constructor */
    public AIPlayerRuleBased(Board board) {
       super(board);
    }
 
    /**
     * Compute the next move based on rule-based strategy.
     * @return int[2] {row, col} of the best move.
     */
    @Override
    public int[] move() {
       // Rule 1: If AI has a winning move, take it
       int[] winningMove = findWinningMove(mySeed);
       if (winningMove != null) return winningMove;
 
       // Rule 2: If opponent has a winning move, block it
       int[] blockingMove = findWinningMove(oppSeed);
       if (blockingMove != null) return blockingMove;
 
       // Rule 3: If AI can create a fork, do it
       int[] forkMove = findForkMove(mySeed);
       if (forkMove != null) return forkMove;
 
       // Rule 4: Prevent opponent from creating a fork
       int[] preventForkMove = findForkMove(oppSeed);
       if (preventForkMove != null) return preventForkMove;
 
       // Rule 5: Choose the move with maximum winning opportunities
       return findBestMove();
    }
 
    /**
     * Find a winning move for the given seed.
     * @return int[2] {row, col} of the winning move, or null if none.
     */
    private int[] findWinningMove(Seed seed) {
       for (int row = 0; row < ROWS; row++) {
          for (int col = 0; col < COLS; col++) {
             if (cells[row][col].content == Seed.EMPTY) {
                cells[row][col].content = seed; // Try the move
                if (hasWon(seed, row, col)) {
                   cells[row][col].content = Seed.EMPTY; // Undo
                   return new int[]{row, col};
                }
                cells[row][col].content = Seed.EMPTY; // Undo
             }
          }
       }
       return null;
    }
 
    /**
     * Check if the given seed has won after placing at (row, col).
     * @return true if the given seed wins.
     */
    private boolean hasWon(Seed seed, int row, int col) {
       return (cells[row][0].content == seed && cells[row][1].content == seed && cells[row][2].content == seed) || // Row
              (cells[0][col].content == seed && cells[1][col].content == seed && cells[2][col].content == seed) || // Column
              (row == col && cells[0][0].content == seed && cells[1][1].content == seed && cells[2][2].content == seed) || // Diagonal
              (row + col == 2 && cells[0][2].content == seed && cells[1][1].content == seed && cells[2][0].content == seed); // Anti-diagonal
    }
 
    /**
     * Find a fork move for the given seed.
     * @return int[2] {row, col} of the fork move, or null if none.
     */
    private int[] findForkMove(Seed seed) {
       for (int row = 0; row < ROWS; row++) {
          for (int col = 0; col < COLS; col++) {
             if (cells[row][col].content == Seed.EMPTY) {
                cells[row][col].content = seed; // Try the move
                int winningWays = countWinningWays(seed);
                cells[row][col].content = Seed.EMPTY; // Undo
                if (winningWays > 1) {
                   return new int[]{row, col};
                }
             }
          }
       }
       return null;
    }
 
    /**
     * Count the number of winning ways for the given seed.
     * @return Number of possible winning ways.
     */
    private int countWinningWays(Seed seed) {
       int count = 0;
       // Count rows
       for (int row = 0; row < ROWS; row++) {
          if (isWinningLine(seed, cells[row][0], cells[row][1], cells[row][2])) count++;
       }
       // Count columns
       for (int col = 0; col < COLS; col++) {
          if (isWinningLine(seed, cells[0][col], cells[1][col], cells[2][col])) count++;
       }
       // Count diagonals
       if (isWinningLine(seed, cells[0][0], cells[1][1], cells[2][2])) count++;
       if (isWinningLine(seed, cells[0][2], cells[1][1], cells[2][0])) count++;
       return count;
    }
 
    /**
     * Check if the given line can lead to a win for the given seed.
     * @return true if the line is a potential win.
     */
    private boolean isWinningLine(Seed seed, Cell cell1, Cell cell2, Cell cell3) {
       int countSeed = 0, countEmpty = 0;
       if (cell1.content == seed) countSeed++;
       if (cell2.content == seed) countSeed++;
       if (cell3.content == seed) countSeed++;
       if (cell1.content == Seed.EMPTY) countEmpty++;
       if (cell2.content == Seed.EMPTY) countEmpty++;
       if (cell3.content == Seed.EMPTY) countEmpty++;
       return countSeed > 0 && countEmpty == 3 - countSeed;
    }
 
    /**
     * Find the move that maximizes winning opportunities.
     * @return int[2] {row, col} of the best move.
     */
    private int[] findBestMove() {
       int maxWays = -1;
       int[] bestMove = null;
       for (int row = 0; row < ROWS; row++) {
          for (int col = 0; col < COLS; col++) {
             if (cells[row][col].content == Seed.EMPTY) {
                cells[row][col].content = mySeed; // Try the move
                int winningWays = countWinningWays(mySeed);
                cells[row][col].content = Seed.EMPTY; // Undo
                if (winningWays > maxWays) {
                   maxWays = winningWays;
                   bestMove = new int[]{row, col};
                }
             }
          }
       }
       return bestMove;
    }
 }
