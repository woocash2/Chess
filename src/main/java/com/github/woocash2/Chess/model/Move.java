package com.github.woocash2.Chess.model;

public class Move {

    public Move(int fx, int fy, int tx, int ty) {
        fromX = fx;
        fromY = fy;
        toX = tx;
        toY = ty;
    }

    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public double fromXScreen() {
        return fromY * 100.0 + 50;
    }

    public double fromYScreen() {
        return fromX * 100 + 50;
    }

    public double toXScreen() {
        return toY * 100 + 50;
    }

    public double toYScreen() {
        return toX * 100 + 50;
    }
}
