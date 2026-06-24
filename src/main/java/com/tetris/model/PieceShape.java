package com.tetris.model;

import lombok.Getter;

import java.awt.*;

@Getter
public enum PieceShape {
    EMPTY(new int[][]{{0}}, Color.BLACK),

    I(new int[][] {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0 ,0 ,0},
            {0, 0, 0, 0}
    }, Color.CYAN),

    J(new int[][] {
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
    }, Color.BLUE),

    L(new int[][] {
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
    }, Color.ORANGE),

    O(new int[][] {
            {1, 1},
            {1, 1}
    }, Color.YELLOW),

    S(new int[][] {
            {0, 1, 1},
            {1, 1, 0},
            {0, 0, 0}
    }, Color.GREEN),

    T(new int[][] {
            {0, 1, 0},
            {1, 1, 1},
            {0, 0, 0}
    }, Color.MAGENTA),

    Z(new int[][] {
            {1, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
    }, Color.RED);

    private final Color color;
    private final int[][] array;

    PieceShape(int[][] array, Color color) {
        this.array = array;
        this.color = color;
    }

}
