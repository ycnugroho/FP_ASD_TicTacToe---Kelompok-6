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
 
