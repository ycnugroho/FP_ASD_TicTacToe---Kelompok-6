/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231030 - Jonathan Abimanyu Trisno
 * 2 - 5026231032 - Yokanan Prawira Nugroho
 * 3 - 5026231133 - Muhammad Rifqi Alfareza Santosa
 */

//Untuk menjalankan TicTacToe 3x3 melalui class TicTacToeGUI.java
package TicTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private AIPlayer aiPlayer;
    private boolean isHumanVsHuman;
    private JPanel bottomPanel;  // New panel for status and return button
    private JButton returnButton;  // New return button
    private CardLayout cardLayout;  // Reference to the main CardLayout
    private JPanel mainPanel;     // Reference to the main panel

    public GameMain(boolean isHumanVsHuman, CardLayout cardLayout, JPanel mainPanel) {
        this.isHumanVsHuman = isHumanVsHuman;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        
                        if (currentState == State.PLAYING) {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                            
                            if (!isHumanVsHuman && aiPlayer != null && currentPlayer == aiPlayer.mySeed) {
                                Timer timer = new Timer(200, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        int[] move = aiPlayer.move();
                                        if (move != null && move.length == 2) {
                                            currentState = board.stepGame(currentPlayer, move[0], move[1]);
                                            if (currentState == State.PLAYING) {
                                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                            }
                                            repaint();
                                        }
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();
                            }
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        // Create return button with custom styling
        returnButton = new JButton("Return to Menu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2d.setColor(new Color(64, 154, 225));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        returnButton.setPreferredSize(new Dimension(120, 25));
        returnButton.setBorderPainted(false);
        returnButton.setFocusPainted(false);
        returnButton.setContentAreaFilled(false);
        
        returnButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Menu");
        });

        // Create status bar
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(180, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        // Create bottom panel to hold both status and return button
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        bottomPanel.setBackground(COLOR_BG_STATUS);
        bottomPanel.add(statusBar);
        bottomPanel.add(returnButton);

        super.setLayout(new BorderLayout());
        super.add(bottomPanel, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();
    }

    public void initGame() {
        board = new Board();
        if (!isHumanVsHuman) {
            aiPlayer = new AIPlayerEnhanced(board);
            aiPlayer.setSeed(Seed.NOUGHT);
        } else {
            aiPlayer = null;
        }
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            if (isHumanVsHuman) {
                statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
            } else {
                statusBar.setText((currentPlayer == Seed.CROSS) ? "Your Turn" : "Computer's Turn");
            }
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }
}
