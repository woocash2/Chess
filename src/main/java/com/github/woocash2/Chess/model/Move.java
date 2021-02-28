package com.github.woocash2.Chess.model;

public class Move {

    public Move(int fx, int fy, int tx, int ty) {
        fromX = fx;
        fromY = fy;
        toX = tx;
        toY = ty;
    }

    public Move(int fx, int fy, int tx, int ty, Board.Piece pr, Board.Piece po, Board.Team t, Board.Piece ta) {
        this(fx, fy, tx, ty);
        pre = pr;
        post = po;
        team = t;
        taken = ta;
    }

    public Move(Move m) {
        fromX = m.fromX;
        fromY = m.fromY;
        toX = m.toX;
        toY = m.toY;
        pre = m.pre;
        post = m.post;
        team = m.team;
        taken = m.taken;
    }

    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public Board.Piece pre = null;
    public Board.Piece post = null;
    public Board.Team team = null;
    public Board.Piece taken = null;

    @Override
    public String toString() {
        return fromX + " " + fromY + " " + toX + " " + toY + " " + pre + " " + post + " " + taken + " " + team;
    }
}
