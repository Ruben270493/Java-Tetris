package com.tetris;

import com.tetris.model.Piece;
import com.tetris.model.PieceShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board extends JPanel implements ActionListener {

    private final int ROWS = 20;
    private final int COLUMNS = 10;
    private final int BOX_SIZE = 30;

    private Timer timer;
    private Piece activePiece;

    private Color[][] background = new Color[ROWS][COLUMNS];
    private List<PieceShape> piecesBag = new ArrayList<>();
    private boolean gameOver = false;
    private int points = 0;
    private int totalCleanLines = 0;

    public Board() {
        setPreferredSize(new Dimension(COLUMNS * BOX_SIZE, ROWS * BOX_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyboardControl());
        activePiece = new Piece(getRandomPieceShape());

        timer = new Timer(500, this);
        timer.start();
    }

    private PieceShape getRandomPieceShape() {
        if (piecesBag.isEmpty()) {
            for (PieceShape shape : PieceShape.values()) {
                if (shape != PieceShape.EMPTY) {
                    piecesBag.add(shape);
                }
            }

            Collections.shuffle(piecesBag);
        }

        return piecesBag.remove(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                g.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
            }
        }

        for (int fila = 0; fila < ROWS; fila++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (background[fila][col] != null) {
                    g.setColor(background[fila][col]);
                    g.fillRect(col * BOX_SIZE, fila * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * BOX_SIZE, fila * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Total score: " + points, 10, 25);

        if (activePiece != null) {
            g.setColor(activePiece.getColor());
            int[][] matriz = activePiece.getArray();

            for (int fila = 0; fila < matriz.length; fila++) {
                for (int col = 0; col < matriz[fila].length; col++) {
                    if (matriz[fila][col] == 1) {
                        int dibujoX = (activePiece.getX() + col) * BOX_SIZE;
                        int dibujoY = (activePiece.getY() + fila) * BOX_SIZE;

                        g.fillRect(dibujoX, dibujoY, BOX_SIZE, BOX_SIZE);
                        g.setColor(Color.BLACK);
                        g.drawRect(dibujoX, dibujoY, BOX_SIZE, BOX_SIZE);
                        g.setColor(activePiece.getColor());
                    }
                }
            }
        }

        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));

            String texto = "GAME OVER!";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(texto)) / 2;
            int y = getHeight() / 2;

            g.drawString(texto, x, y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        if (!checkCollision(activePiece.getX(), activePiece.getY() + 1, activePiece.getArray())) {
            activePiece.fall();
        } else {
            fixOnTheBottom();
            clearRows();
            Piece newPiece = new Piece(getRandomPieceShape());

            if (checkCollision(newPiece.getX(), newPiece.getY(), newPiece.getArray())) {
                gameOver = true;
                timer.stop();
                System.out.println("GAME OVER!");
            }

            activePiece = new Piece(getRandomPieceShape());
        }
        repaint();
    }

    private void fixOnTheBottom() {
        int[][] matriz = activePiece.getArray();
        for (int row = 0; row < matriz.length; row++) {
            for (int col = 0; col < matriz[row].length; col++) {
                if (matriz[row][col] == 1) {
                    int boardX = activePiece.getX() + col;
                    int boardY = activePiece.getY() + row;

                    // Guardamos el color de la pieza en el fondo estático
                    if (boardY >= 0 && boardY < ROWS && boardX >= 0 && boardX < COLUMNS) {
                        background[boardY][boardX] = activePiece.getColor();
                    }
                }
            }
        }
    }

    private class KeyboardControl extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameOver) { return; }

            int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_LEFT:
                    if (!checkCollision(activePiece.getX() - 1, activePiece.getY(), activePiece.getArray())) {
                        activePiece.moveLeft();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!checkCollision(activePiece.getX() + 1, activePiece.getY(), activePiece.getArray())) {
                    activePiece.moveRight();
                    }
                    break;
                case KeyEvent.VK_UP:
                    activePiece.turnRight();
                    if (checkCollision(activePiece.getX(), activePiece.getY(), activePiece.getArray())) {
                        activePiece.turnRight();
                        activePiece.turnRight();
                        activePiece.turnRight();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!checkCollision(activePiece.getX(), activePiece.getY() + 1, activePiece.getArray())) {
                        activePiece.fall();
                    }
                    break;
            }

            repaint();
        }
    }

    public boolean checkCollision(int futureX, int futureY, int[][] futureArray) {
        for(int row = 0; row < futureArray.length; row++) {
            for (int col = 0; col < futureArray[row].length; col++) {
                if (futureArray[row][col] == 1) {
                    int boardX = futureX + col;
                    int boardY = futureY + row;

                    if (boardX < 0 || boardX >= COLUMNS || boardY >= ROWS) {
                        return true;
                    }

                    if (boardY < 0)  {
                        continue;
                    }

                    if (background[boardY][boardX] != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void clearRows() {
        int linesCleanInThisTurn = 0;

        for (int row = ROWS - 1; row >= 0; row--) {
            boolean filaCompleta = true;

            for (int col = 0; col < ROWS; col++) {
                System.out.println("Row: " + row + " Col: " + col + " ");
                if (col < COLUMNS && background[row][col] == null) {
                    filaCompleta = false;
                    break;
                }
            }

            if (filaCompleta) {
                linesCleanInThisTurn++;

                for (int f = row; f > 0; f--) {
                    for (int c = 0; c < COLUMNS; c++) {
                        background[f][c] = background[f - 1][c];
                    }
                }

                for (int c = 0; c < COLUMNS; c++) {
                    background[0][c] = null;
                }

                if (row < ROWS - 1) {
                    row++;
                }
            }
        }

        if (linesCleanInThisTurn > 0) {
            addPoints(linesCleanInThisTurn);
        }
    }

    private void addPoints(int lines) {
        totalCleanLines += lines;

        switch (lines) {
            case 1: points += 100; break;
            case 2: points += 300; break;
            case 3: points += 500; break;
            case 4: points += 800; break;
        }

        System.out.println("Líneas borradas: " + lines + " | Puntuación total: " + points);
    }

}
