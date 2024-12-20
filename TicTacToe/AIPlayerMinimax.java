package TicTacToe;

import java.util.*;

/** AIPlayer using Minimax algorithm */
public class AIPlayerMinimax extends AIPlayer {
 
    /** Constructor with the given game board */
    public AIPlayerMinimax(Board board) {
        super(board);
    }
 
    /** Get next best move for computer. Return int[2] of {row, col} */
    @Override
    public int[] move() {
        int[] result = minimax(2, mySeed); // depth, max turn
        System.out.println("AI Move: Row = " + result[1] + ", Col = " + result[2]); // Debugging output
        return new int[] {result[1], result[2]};   // row, col
    }
 
    /** Recursive minimax at level of depth for either maximizing or minimizing player.
        Return int[3] of {score, row, col}  */
        private int[] minimax(int depth, Seed player) {
            List<int[]> nextMoves = generateMoves();
            int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            int currentScore;
            int bestRow = -1;
            int bestCol = -1;
        
            if (nextMoves.isEmpty() || depth == 0) {
                bestScore = evaluate();
                System.out.println("Evaluating at depth " + depth + ": Score = " + bestScore); // Debugging output
            } else {
                for (int[] move : nextMoves) {
                    cells[move[0]][move[1]].content = player; // Try this move
                    currentScore = minimax(depth - 1, (player == mySeed) ? oppSeed : mySeed)[0];
                    System.out.println("Move: (" + move[0] + ", " + move[1] + ") Score: " + currentScore); // Debugging output
        
                    if (player == mySeed) {
                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestRow = move[0];
                            bestCol = move[1];
                        }
                    } else {
                        if (currentScore < bestScore) {
                            bestScore = currentScore;
                            bestRow = move[0];
                            bestCol = move[1];
                        }
                    }
                    cells[move[0]][move[1]].content = Seed.EMPTY; // Undo move
                }
            }
            return new int[] {bestScore, bestRow, bestCol};
        }
 
    /** Find all valid next moves.
        Return List of moves in int[2] of {row, col} or empty list if gameover */
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List
 
        // If gameover, i.e., no next move
        if (hasWon(mySeed) || hasWon(oppSeed)) {
            return nextMoves;   // return empty list
        }
 
        // Search for empty cells and add to the List
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.EMPTY) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }
        return nextMoves;
    }
 
    /** The heuristic evaluation function for the current board
        @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
                -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
                0 otherwise   */
    private int evaluate() {
     int score = 0;
     // Evaluasi skor untuk setiap baris, kolom, dan diagonal
        score += evaluateLine(0, 0, 0, 1, 0, 2); // baris 0
        score += evaluateLine(1, 0, 1, 1, 1, 2); // baris 1
        score += evaluateLine(2, 0, 2, 1, 2, 2); // baris 2
        score += evaluateLine(0, 0, 1, 0, 2, 0); // kolom 0
        score += evaluateLine(0, 1, 1, 1, 2, 1); // kolom 1
        score += evaluateLine(0, 2, 1, 2, 2, 2); // kolom 2
        score += evaluateLine(0, 0, 1, 1, 2, 2); // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0); // diagonal alternatif
                
        System.out.println("Evaluating board: Score = " + score); // Debugging output
        return score;
    }
 
    /** The heuristic evaluation function for the given line of 3 cells
        @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
                -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
                0 otherwise */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;
 
        // First cell
        if (cells[row1][col1].content == mySeed) {
            score = 1;
        } else if (cells[row1][col1].content == oppSeed) {
            score = -1;
        }
 
        // Second cell
        if (cells[row2][col2].content == mySeed) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (cells[row2][col2].content == oppSeed) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }
 
        // Third cell
        if (cells[row3][col3].content == mySeed) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (cells[row3][col3].content == oppSeed) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }
 
    private int[] winningPatterns = {
        0b111000000, 0b000111000, 0b000000111, // rows
        0b100100100, 0b010010010, 0b001001001, // cols
        0b100010001, 0b001010100               // diagonals
    };
 
    /** Returns true if thePlayer wins */
    private boolean hasWon(Seed thePlayer) {
        int pattern = 0b000000000;  // 9-bit pattern for the 9 cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == thePlayer) {
                    pattern |= (1 << (row * COLS + col));
                }
            }
        }
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }
}
