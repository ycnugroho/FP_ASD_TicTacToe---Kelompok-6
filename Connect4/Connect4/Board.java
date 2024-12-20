/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231030 - Jonathan Abimanyu Trisno
 * 2 - 5026231032 - Yokanan Prawira Nugroho
 * 3 - 5026231133 - Muhammad Rifqi Alfareza Santosa
 */

package Connect4;

import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
   // Define named constants
   public static final int ROWS = 6;  // ROWS x COLS cells
   public static final int COLS = 7;
   // Define named constants for drawing
   public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
   public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
   public static final int GRID_WIDTH = 8;  // Grid-line's width
   public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
   public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
   public static final int Y_OFFSET = 1;  // Fine tune for better display

   // Define properties (package-visible)
   /** Composes of 2D array of ROWS-by-COLS Cell instances */
   Cell[][] cells;

   /** Constructor to initialize the game board */
   public Board() {
      initGame();
   }

   /** Initialize the game objects (run once) */
   public void initGame() {
      cells = new Cell[ROWS][COLS]; // allocate the array
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            // Allocate element of the array
            cells[row][col] = new Cell(row, col);
               // Cells are initialized in the constructor
         }
      }
   }

   /** Reset the game board, ready for new game */
   public void newGame() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].newGame(); // clear the cell content
         }
      }
   }

   /**
    *  The given player makes a move on (selectedRow, selectedCol).
    *  Update cells[selectedRow][selectedCol]. Compute and return the
    *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
    */

    public boolean hasWon(Seed player, int row, int col) {
      // Check horizontal
      for (int c = Math.max(0, col - 3); c <= Math.min(COLS - 4, col); c++) {
         if (cells[row][c].content == player &&
             cells[row][c + 1].content == player &&
             cells[row][c + 2].content == player &&
             cells[row][c + 3].content == player) {
            return true;
         }
      }
      // Check vertical
      for (int r = Math.max(0, row - 3); r <= Math.min(ROWS - 4, row); r++) {
         if (cells[r][col].content == player &&
             cells[r + 1][col].content == player &&
             cells[r + 2][col].content == player &&
             cells[r + 3][col].content == player) {
            return true;
         }
      }
      // Check diagonal (\)
      for (int r = Math.max(0, row - 3), c = Math.max(0, col - 3);
           r <= Math.min(ROWS - 4, row) && c <= Math.min(COLS - 4, col);
           r++, c++) {
         if (cells[r][c].content == player &&
             cells[r + 1][c + 1].content == player &&
             cells[r + 2][c + 2].content == player &&
             cells[r + 3][c + 3].content == player) {
            return true;
         }
      }
      // Check diagonal (/)
      for (int r = Math.min(ROWS - 1, row + 3), c = Math.max(0, col - 3);
           r >= Math.max(3, row) && c <= Math.min(COLS - 4, col);
           r--, c++) {
         if (cells[r][c].content == player &&
             cells[r - 1][c + 1].content == player &&
             cells[r - 2][c + 2].content == player &&
             cells[r - 3][c + 3].content == player) {
            return true;
         }
      }
      return false;
   }

   public State stepGame(Seed player, int selectedRow, int selectedCol) {
      // Update game board
      cells[selectedRow][selectedCol].content = player;

      // Check for win
      if (hasWon(player, selectedRow, selectedCol)) {
         return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
      }

      // Check for draw (board is full)
      for (int r = 0; r < ROWS; r++) {
         for (int c = 0; c < COLS; c++) {
            if (cells[r][c].content == Seed.NO_SEED) {
               return State.PLAYING; // At least one empty cell, game continues
            }
         }
      }   

      // If no empty cells and no win, it's a draw
      return State.DRAW;
   }

   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g) {
      // Draw the grid-lines
      g.setColor(COLOR_GRID);
      for (int row = 1; row < ROWS; ++row) {
         g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
               CANVAS_WIDTH - 1, GRID_WIDTH,
               GRID_WIDTH, GRID_WIDTH);
      }
      for (int col = 1; col < COLS; ++col) {
         g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
               GRID_WIDTH, CANVAS_HEIGHT - 1,
               GRID_WIDTH, GRID_WIDTH);
      }

      // Draw all the cells
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].paint(g);  // ask the cell to paint itself
         }
      }
   }
}