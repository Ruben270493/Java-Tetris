package com.tetris;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    public Game() {
        setTitle("Java Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Board board = new Board();
        add(board);
        pack();

        setLocationRelativeTo(null);
    }

    static void main() {
        EventQueue.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }

}
