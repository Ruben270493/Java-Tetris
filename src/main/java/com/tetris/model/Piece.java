package com.tetris.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Piece {

    private int x;
    private int y;
    private int[][] array;
    private PieceShape shape;

    public Piece(PieceShape shape) {
        this.shape = shape;
        this.array = cloneArray(shape.getArray());

        this.x = 5 - (array.length / 2);
        this.y = 0;
    }

    public void turnRight() {
        int n = array.length;
        int[][] newArray = new int[n][n];

        for (int fila = 0; fila < n; fila++) {
            for (int col = 0; col < n; col++) {
                newArray[col][n - 1 - fila] = array[fila][col];
            }
        }
        this.array = newArray;
    }

    private int[][] cloneArray(int[][] originalArray) {
        int[][] copy = new int[originalArray.length][];

        for (int i = 0; i < originalArray.length; i++) {
            copy[i] = originalArray[i].clone();
        }

        return copy;
    }


    public void moveLeft() { x--; }
    public void moveRight() { x++; }
    public void fall() { y++; }
    public void up() { y--; }

    public Color getColor() {
        return this.shape.getColor();
    }
}
