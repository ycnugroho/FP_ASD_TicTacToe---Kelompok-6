/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231030 - Jonathan Abimanyu Trisno
 * 2 - 5026231032 - Yokanan Prawira Nugroho
 * 3 - 5026231133 - Muhammad Rifqi Alfareza Santosa
 */

package TicTacToe;

/**
 * Enhanced AI player using Minimax algorithm with Alpha-Beta pruning
 */
public class AIPlayerEnhanced extends AIPlayer {
    private static final int MAX_DEPTH = 6;
    
    /** Constructor */
    public AIPlayerEnhanced(Board board) {
        super(board);
    }
    
    @Override
    public int[] move() {
        // Get the best move using minimax
        int[] bestMove = minimax(0, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{bestMove[1], bestMove[2]};
    }
    
    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        // Initialize best score and move
        int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestRow = -1;
        int bestCol = -1;
        
        // If terminal state or max depth reached, evaluate board
        if (isGameOver() || depth == MAX_DEPTH) {
            bestScore = evaluateBoard();
            return new int[]{bestScore, bestRow, bestCol};
        }
        
        // Try each possible move
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {  // Changed from EMPTY to NO_SEED
                    // Make move
                    cells[row][col].content = player;
                    
                    // Recursively evaluate position
                    int score;
                    if (player == mySeed) {
                        score = minimax(depth + 1, oppSeed, alpha, beta)[0];
                        if (score > bestScore) {
                            bestScore = score;
                            bestRow = row;
                            bestCol = col;
                            alpha = Math.max(alpha, bestScore);
                        }
                    } else {
                        score = minimax(depth + 1, mySeed, alpha, beta)[0];
                        if (score < bestScore) {
                            bestScore = score;
                            bestRow = row;
                            bestCol = col;
                            beta = Math.min(beta, bestScore);
                        }
                    }
                    
                    // Undo move
                    cells[row][col].content = Seed.NO_SEED;  // Changed from EMPTY to NO_SEED
                    
                    // Alpha-beta pruning
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        
        return new int[]{bestScore, bestRow, bestCol};
    }
    
    private boolean isGameOver() {
        return hasWon(mySeed) || hasWon(oppSeed) || isDraw();
    }
    
    private boolean isDraw() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {  // Changed from EMPTY to NO_SEED
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean hasWon(Seed seed) {
        // Check rows
        for (int row = 0; row < ROWS; row++) {
            if (cells[row][0].content == seed 
                && cells[row][1].content == seed 
                && cells[row][2].content == seed) {
                return true;
            }
        }
        
        // Check columns
        for (int col = 0; col < COLS; col++) {
            if (cells[0][col].content == seed 
                && cells[1][col].content == seed 
                && cells[2][col].content == seed) {
                return true;
            }
        }
        
        // Check diagonals
        if (cells[0][0].content == seed 
            && cells[1][1].content == seed 
            && cells[2][2].content == seed) {
            return true;
        }
        
        if (cells[0][2].content == seed 
            && cells[1][1].content == seed 
            && cells[2][0].content == seed) {
            return true;
        }
        
        return false;
    }
    
    private int evaluateBoard() {
        if (hasWon(mySeed)) return 10;
        if (hasWon(oppSeed)) return -10;
        return 0;
    }
}
