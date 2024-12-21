/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231030 - Jonathan Abimanyu Trisno
 * 2 - 5026231032 - Yokanan Prawira Nugroho
 * 3 - 5026231133 - Muhammad Rifqi Alfareza Santosa
 */

/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231030 - Jonathan Abimanyu Trisno
 * 2 - 5026231032 - Yokanan Prawira Nugroho
 * 3 - 5026231133 - Muhammad Rifqi Alfareza Santosa
 */

 import java.awt.*;
 import java.awt.event.*;
 import javax.swing.*;
 
 public class ConnectFour extends JFrame {
     private static final long serialVersionUID = 1L;
 
     public static final int ROWS = 6;
     public static final int COLS = 7;
 
     public static final int CELL_SIZE = 120;
     public static final int BOARD_WIDTH = CELL_SIZE * COLS;
     public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
     public static final int GRID_WIDTH = 10;
     public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
     public static final int CELL_PADDING = CELL_SIZE / 5;
     public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
     public static final int SYMBOL_STROKE_WIDTH = 8;
     public static final Color COLOR_BG = Color.WHITE;
     public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
     public static final Color COLOR_GRID = Color.LIGHT_GRAY;
     public static final Color COLOR_CROSS = new Color(211, 45, 65);
     public static final Color COLOR_NOUGHT = new Color(76, 181, 245);
     public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);
 
     public enum State {
         PLAYING, DRAW, CROSS_WON, NOUGHT_WON
     }
 
     public enum Seed {
         CROSS, NOUGHT, EMPTY
     }
 
     private State currentState;
     private Seed currentPlayer;
     private Seed[][] board;
     private GamePanel gamePanel;
     private JLabel statusBar;
 
     public ConnectFour() {
         initGame();
 
         gamePanel = new GamePanel();
         gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
 
         gamePanel.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 if (currentState == State.PLAYING) {
                     int mouseX = e.getX();
                     int colSelected = mouseX / CELL_SIZE;
 
                     if (colSelected >= 0 && colSelected < COLS) {
                         for (int row = ROWS - 1; row >= 0; row--) {
                             if (board[row][colSelected] == Seed.EMPTY) {
                                 board[row][colSelected] = currentPlayer;
                                 updateGame(currentPlayer, row, colSelected);
                                 currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                 break;
                             }
                         }
                     }
                 } else {
                     newGame(); // Start a new game if the game is over
                 }
                 repaint();
             }
         });
 
         statusBar = new JLabel("       ");
         statusBar.setFont(FONT_STATUS);
         statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
         statusBar.setOpaque(true);
         statusBar.setBackground(COLOR_BG_STATUS);
 
         Container cp = getContentPane();
         cp.setLayout(new BorderLayout());
         cp.add(gamePanel, BorderLayout.CENTER);
         cp.add(statusBar, BorderLayout.PAGE_END);
 
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         pack();
         setTitle("Connect Four");
         setVisible(true);
 
         newGame();
     }
 
     public void initGame() {
         board = new Seed[ROWS][COLS];
     }
 
     public void newGame() {
         for (int row = 0; row < ROWS; row++) {
             for (int col = 0; col < COLS; col++) {
                 board[row][col] = Seed.EMPTY;
             }
         }
         currentPlayer = Seed.CROSS;
         currentState = State.PLAYING;
     }
 
     public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
         // Check horizontal
         int count = 0;
         for (int col = 0; col < COLS; col++) {
             if (board[rowSelected][col] == theSeed) {
                 count++;
                 if (count == 4) return true;
             } else {
                 count = 0;
             }
         }
 
         // Check vertical
         count = 0;
         for (int row = 0; row < ROWS; row++) {
             if (board[row][colSelected] == theSeed) {
                 count++;
                 if (count == 4) return true;
             } else {
                 count = 0;
             }
         }
 
         // Check diagonal (bottom-left to top-right)
         for (int row = 3; row < ROWS; row++) {
             for (int col = 0; col < COLS - 3; col++) {
                 if (board[row][col] == theSeed &&
                     board[row-1][col+1] == theSeed &&
                     board[row-2][col+2] == theSeed &&
                     board[row-3][col+3] == theSeed) {
                     return true;
                 }
             }
         }
 
         // Check diagonal (top-left to bottom-right)
         for (int row = 0; row < ROWS - 3; row++) {
             for (int col = 0; col < COLS - 3; col++) {
                 if (board[row][col] == theSeed &&
                     board[row+1][col+1] == theSeed &&
                     board[row+2][col+2] == theSeed &&
                     board[row+3][col+3] == theSeed) {
                     return true;
                 }
             }
         }
 
         return false;
     }
 
     public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
         if (hasWon(theSeed, rowSelected, colSelected)) {
             currentState = (theSeed == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
         } else {
             // Check for draw
             boolean isDraw = true;
             for (int col = 0; col < COLS; col++) {
                 if (board[0][col] == Seed.EMPTY) {
                     isDraw = false;
                     break;
                 }
             }
             if (isDraw) {
                 currentState = State.DRAW;
             }
             // else state remains State.PLAYING
         }
     }
 
     class GamePanel extends JPanel {
         private static final long serialVersionUID = 1L;
 
         @Override
         public void paintComponent(Graphics g) {
             super.paintComponent(g);
             setBackground(COLOR_BG);
 
             // Draw the grid lines
             g.setColor(COLOR_GRID);
             for (int row = 1; row < ROWS; row++) {
                 g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                         BOARD_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
             }
             for (int col = 1; col < COLS; col++) {
                 g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                         GRID_WIDTH, BOARD_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
             }
 
             // Draw the pieces
             Graphics2D g2d = (Graphics2D)g;
             g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH,
                     BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
             for (int row = 0; row < ROWS; row++) {
                 for (int col = 0; col < COLS; col++) {
                     int x1 = col * CELL_SIZE + CELL_PADDING;
                     int y1 = row * CELL_SIZE + CELL_PADDING;
                     if (board[row][col] == Seed.CROSS) {
                         g2d.setColor(COLOR_CROSS);
                         g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                     } else if (board[row][col] == Seed.NOUGHT) {
                         g2d.setColor(COLOR_NOUGHT);
                         g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                     }
                 }
             }
 
             // Print status message
             if (currentState == State.PLAYING) {
                 statusBar.setForeground(Color.BLACK);
                 statusBar.setText((currentPlayer == Seed.CROSS) ? "Red's Turn" : "Blue's Turn");
             } else if (currentState == State.DRAW) {
                 statusBar.setForeground(Color.RED);
                 statusBar.setText("It's a Draw! Click to play again");
             } else if (currentState == State.CROSS_WON) {
                 statusBar.setForeground(Color.RED);
                 statusBar.setText("'Red' Won! Click to play again");
             } else if (currentState == State.NOUGHT_WON) {
                 statusBar.setForeground(Color.RED);
                 statusBar.setText("'Blue' Won! Click to play again");
             }
         }
     }
 
     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new ConnectFour());
     }
 }