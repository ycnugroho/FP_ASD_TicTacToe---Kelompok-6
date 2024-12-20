package TicTacToe;

/**
 * AI player with heuristic board evaluation function.
 */
public class AIPlayerHeuristic extends AIPlayer {

    /** Constructor */
    public AIPlayerHeuristic(Board board) {
       super(board);
    }
 
    /**
     * Evaluates the current board and computes the relative score.
     * @return int Score of the board.
     */
    private int evaluate() {
       int score = 0;
 
       // Evaluate all rows, columns, and diagonals
       // Rows
       for (int row = 0; row < ROWS; row++) {
          score += evaluateLine(cells[row][0], cells[row][1], cells[row][2]);
       }
       // Columns
       for (int col = 0; col < COLS; col++) {
          score += evaluateLine(cells[0][col], cells[1][col], cells[2][col]);
       }
       // Diagonals
       score += evaluateLine(cells[0][0], cells[1][1], cells[2][2]);
       score += evaluateLine(cells[0][2], cells[1][1], cells[2][0]);
 
       return score;
    }
 
    /**
     * Evaluate a single line of three cells and compute its score.
     * @return int Score of the line.
     */
    private int evaluateLine(Cell cell1, Cell cell2, Cell cell3) {
       int score = 0;
 
       // First cell
       if (cell1.content == mySeed) {
          score = 1;
       } else if (cell1.content == oppSeed) {
          score = -1;
       }
 
       // Second cell
       if (cell2.content == mySeed) {
          if (score == 1) { // cell1 is mySeed
             score = 10;
          } else if (score == -1) { // cell1 is oppSeed
             return 0; // Mixed line
          } else { // cell1 is empty
             score = 1;
          }
       } else if (cell2.content == oppSeed) {
          if (score == -1) { // cell1 is oppSeed
             score = -10;
          } else if (score == 1) { // cell1 is mySeed
             return 0; // Mixed line
          } else { // cell1 is empty
             score = -1;
          }
       }
 
       // Third cell
       if (cell3.content == mySeed) {
          if (score > 0) { // cell1 and/or cell2 is mySeed
             score *= 10;
          } else if (score < 0) { // cell1 and/or cell2 is oppSeed
             return 0; // Mixed line
          } else { // cell1 and cell2 are empty
             score = 1;
          }
       } else if (cell3.content == oppSeed) {
          if (score < 0) { // cell1 and/or cell2 is oppSeed
             score *= 10;
          } else if (score > 0) { // cell1 and/or cell2 is mySeed
             return 0; // Mixed line
          } else { // cell1 and cell2 are empty
             score = -1;
          }
       }
 
       return score;
    }
 
    /**
     * Compute all valid moves and select the one with the highest score.
     * @return int[2] {row, col} of the best move.
     */
    @Override
    public int[] move() {
       int bestScore = Integer.MIN_VALUE;
       int[] bestMove = new int[2];
 
       for (int row = 0; row < ROWS; row++) {
          for (int col = 0; col < COLS; col++) {
             if (cells[row][col].content == Seed.EMPTY) {
                // Try the move
                cells[row][col].content = mySeed;
                int moveScore = evaluate();
                // Undo the move
                cells[row][col].content = Seed.EMPTY;
 
                if (moveScore > bestScore) {
                   bestScore = moveScore;
                   bestMove[0] = row;
                   bestMove[1] = col;
                }
             }
          }
       }
 
       return bestMove;
    }
 }
