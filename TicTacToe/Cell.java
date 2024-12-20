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

import java.awt.*;
import java.awt.geom.*;

public class Cell {
    public static final int SIZE = 120;
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;
    public static final int SEED_STROKE_WIDTH = 8;

    Seed content;
    int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    public void newGame() {
        content = Seed.NO_SEED;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(SEED_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int x = col * SIZE + PADDING;
        int y = row * SIZE + PADDING;

        if (content == Seed.CROSS) {
            drawCustomCross(g2d, x, y);
        } else if (content == Seed.NOUGHT) {
            drawCustomNought(g2d, x, y);
        }
    }

    // Method to draw a custom X symbol
    private void drawCustomCross(Graphics2D g2d, int x, int y) {
        g2d.setColor(GameMain.COLOR_CROSS);
        
        int size = SEED_SIZE;
        // Option 1: Star-like X
        int halfSize = size / 2;
        int centerX = x + halfSize;
        int centerY = y + halfSize;
        
        // Draw main X
        g2d.drawLine(x, y, x + size, y + size);
        g2d.drawLine(x + size, y, x, y + size);
        
        // Draw additional decorative lines
        g2d.drawLine(centerX, y, centerX, y + size);
        g2d.drawLine(x, centerY, x + size, centerY);
      

      //   // Option 2: Curved X
      //   int curve = 20;
      //   g2d.draw(new CubicCurve2D.Double(
      //       x, y,
      //       x + curve, y + curve,
      //       x + size - curve, y + size - curve,
      //       x + size, y + size
      //   ));
      //   g2d.draw(new CubicCurve2D.Double(
      //       x + size, y,
      //       x + size - curve, y + curve,
      //       x + curve, y + size - curve,
      //       x, y + size
      //   ));
    }

    // Method to draw a custom O symbol
    private void drawCustomNought(Graphics2D g2d, int x, int y) {
        g2d.setColor(GameMain.COLOR_NOUGHT);
        
        /* 
        // Option 1: Double Circle O
        int size = SEED_SIZE;
        int innerSize = size - 20;
        
        g2d.drawOval(x, y, size, size);
        g2d.drawOval(x + 10, y + 10, innerSize, innerSize);
      */
        
        // Option 2: Flower O
        int size = SEED_SIZE;
        int petals = 6;
        int centerX = x + size/2;
        int centerY = y + size/2;
        int radius = size/2;
        
        // Draw main circle
        g2d.drawOval(x, y, size, size);
        
        // Draw decorative petals
        for (int i = 0; i < petals; i++) {
            double angle = 2 * Math.PI * i / petals;
            int petalX = (int)(centerX + radius/2 * Math.cos(angle));
            int petalY = (int)(centerY + radius/2 * Math.sin(angle));
            g2d.drawOval(petalX - radius/4, petalY - radius/4, radius/2, radius/2);
        }
    }
}